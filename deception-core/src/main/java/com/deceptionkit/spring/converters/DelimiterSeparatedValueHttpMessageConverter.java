package com.deceptionkit.spring.converters;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Streams.stream;

/**
 * A {@link org.springframework.http.converter.HttpMessageConverter} capable of generating and
 * parsing columnar files with single-character delimiters, such as CSV and TSV files.
 */
public final class DelimiterSeparatedValueHttpMessageConverter extends AbstractGenericHttpMessageConverter<Object> {
    /**
     * Types for which <em>serialization</em> is supported.
     */
    private static final ImmutableList<Class<?>> SUPPORTED_SUPER_TYPES = ImmutableList.of(Iterable.class, Stream.class);

    /**
     * Cached {@link ObjectReader}s, indexed by type.
     */
    private final ConcurrentMap<Class<?>, ObjectReader> readers = new ConcurrentHashMap<>();
    /**
     * Cached {@link ObjectWriter}s, indexed by type.
     */
    private final ConcurrentMap<Class<?>, ObjectWriter> writers = new ConcurrentHashMap<>();
    /**
     * The {@link CsvMapper} to which the heavy lifting is delegated.
     */
    private final CsvMapper csvMapper;
    /**
     * The character used as value delimiter.
     */
    private final char delimiter;
    /**
     * Flag indicating whether a header is expected to be read or written.
     */
    private final boolean includeHeader;

    /**
     * Instantiates a new {@link DelimiterSeparatedValueHttpMessageConverter}.
     *
     * @param supportedMediaTypes The list of {@link MediaType}s that should be supported by this
     *                            converter.
     * @param delimiter           The character to be used as value delimiter.
     * @param includeHeader       Flag indicating whether a header is expected to be read or written.
     * @param csvMapper           The {@link CsvMapper} to which the heavy lifting is delegated.
     */
    private DelimiterSeparatedValueHttpMessageConverter(final ImmutableSet<MediaType> supportedMediaTypes, final char delimiter, final boolean includeHeader, final CsvMapper csvMapper) {
        super(supportedMediaTypes.toArray(new MediaType[0]));
        this.csvMapper = csvMapper;
        this.delimiter = delimiter;
        this.includeHeader = includeHeader;
    }

    /**
     * Creates a default TSV converter supporting the {@link MediaTypes#TEXT_TSV} media type.
     *
     * @param csvMapper {@link CsvMapper} used for de-/serialization.
     * @return A non-{@code null} {@link DelimiterSeparatedValueHttpMessageConverter} configured for
     * TSV files.
     */
    public static DelimiterSeparatedValueHttpMessageConverter csv(final CsvMapper csvMapper) {
        return new DelimiterSeparatedValueHttpMessageConverter(ImmutableSet.of(new MediaType("text", "csv"), MediaType.TEXT_PLAIN), ',', true, csvMapper);
    }

    /**
     * {@inheritDoc}
     *
     * @implNote We override this method because the super implementation provides the <em>context
     * class</em> to the {@link #supports} method. That seems "just plain wrong": the type of
     * the context class is irrelevant.
     * <p>Moreover, we only allow reading super types of {@link ImmutableList}, since {@link
     * #read} always returns an {@link ImmutableList}.
     */
    @Override
    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        return canRead(mediaType) && TypeToken.of(type).getRawType().isAssignableFrom(ImmutableList.class);
    }

    @Override
    protected boolean supports(final Class<?> clazz) {
        return getTableType(clazz).isPresent();
    }

    @Override
    public ImmutableList<?> read(final Type type, final Class<?> contextClazz, final HttpInputMessage inputMessage) throws IOException {
        return ImmutableList.copyOf(getReader(getElementType(type)).readValues(getInputReader(inputMessage)));
    }

    @Override
    protected ImmutableList<?> readInternal(final Class<?> clazz, final HttpInputMessage inputMessage) throws IOException {
        return ImmutableList.copyOf(getReader(getElementType(clazz)).readValues(getInputReader(inputMessage)));
    }

    /**
     * Returns a {@link Reader} containing the contents of the {@link HttpInputMessage}.
     *
     * @param inputMessage the {@link HttpInputMessage} of which the content should be read.
     * @return A non-{@code null} {@link Reader}.
     * @throws IOException If the request body cannot be read.
     */
    private Reader getInputReader(final HttpInputMessage inputMessage) throws IOException {
        /*
         * Respect the charset if specified in the request headers; default to UTF-8 if absent.
         * (Note that we ignore the HTTP 1.1 spec here, which specifies that ISO-8859-1 is the
         * default charset.)
         */
        final Charset contentCharSet = Optional.ofNullable(inputMessage.getHeaders().getContentType()).map(MediaType::getCharset).orElse(StandardCharsets.UTF_8);
        return new InputStreamReader(inputMessage.getBody(), contentCharSet);
    }

    /**
     * Returns the {@link ObjectReader} for instances of the given class.
     *
     * @param clazz The type of instances to deserialize.
     * @return A non-{@code null} {@link ObjectReader}.
     */
    @VisibleForTesting
    public ObjectReader getReader(final Class<?> clazz) {
        return this.readers.computeIfAbsent(clazz, c -> this.csvMapper.reader(getSchema(c)).forType(c));
    }

    @Override
    protected void writeInternal(final Object instance, final Type type, final HttpOutputMessage outputMessage) throws IOException {
        try (SequenceWriter writer = getWriter(getElementType(type)).writeValues(getOutputWriter(outputMessage)); Stream<?> stream = createStream(instance)) {
            Iterator<?> it = stream.iterator();
            while (it.hasNext()) {
                writer.write(it.next());
            }
        }
    }

    /**
     * Returns a {@link Writer} for producing the contents of the given {@link HttpOutputMessage}.
     *
     * @param outputMessage The {@link HttpOutputMessage} to which the content should be written.
     * @return A non-{@code null} {@link Writer}.
     * @throws IOException If the response body cannot be written to.
     */
    private Writer getOutputWriter(final HttpOutputMessage outputMessage) throws IOException {
        /*
         * Respect the charset using the Content-Type header, if any; default to ISO-8859-1
         * otherwise.
         */
        final Charset outputCharset = Optional.ofNullable(outputMessage.getHeaders().getContentType()).map(MediaType::getCharset).orElse(StandardCharsets.ISO_8859_1);
        return new OutputStreamWriter(outputMessage.getBody(), outputCharset);
    }

    /**
     * Returns the {@link ObjectWriter} for instances of the given class.
     *
     * @param clazz The type of instances to serialize.
     * @return A non-{@code null} {@link ObjectWriter}.
     */
    @VisibleForTesting
    public ObjectWriter getWriter(final Class<?> clazz) {
        return this.writers.computeIfAbsent(clazz, c -> this.csvMapper.writer(getSchema(c)));
    }

    /**
     * Returns the {@link CsvSchema} describing the given class.
     *
     * @param clazz The type to be read or written.
     * @return A non-{@code null} {@link CsvSchema}.
     */
    private CsvSchema getSchema(final Class<?> clazz) {
        final CsvSchema schema = this.csvMapper.schemaFor(clazz).withColumnSeparator(this.delimiter);
        return this.includeHeader ? schema.withHeader() : schema;
    }

    /**
     * Returns the element type captured by the given {@link ImmutableList} class.
     *
     * @param type The list type.
     * @return A non-{@code null} {@link Class}.
     */
    private Class<?> getElementType(final Type type) {
        final Class<?> superType = getTableType(type).orElse(null);
        checkArgument(superType != null, "Type %s cannot be represented as a table", type);
        @SuppressWarnings("unchecked") ParameterizedType tableType = (ParameterizedType) TypeToken.of(type).getSupertype((Class) superType).getType();
        return TypeToken.of(tableType.getActualTypeArguments()[0]).getRawType();
    }

    /**
     * Tells whether the given {@link Type} can be serialised as tabular data.
     *
     * @param type The type of interest.
     * @return {@code true} iff the given type represents tabular data.
     */
    private Optional<Class<?>> getTableType(final Type type) {
        return SUPPORTED_SUPER_TYPES.stream().filter(TypeToken.of(type)::isSubtypeOf).findAny();
    }

    /**
     * Returns the given instance as a {@link Stream}.
     *
     * @param instance The object that should be converted to a stream.
     * @return A {@link Stream} that streams the elements of {@code instance}.
     */
    private Stream<?> createStream(final Object instance) {
        if (instance instanceof Stream) {
            return (Stream<?>) instance;
        } else if (instance instanceof Iterable) {
            return stream((Iterable<?>) instance);
        } else {
            throw new IllegalStateException("Type " + instance.getClass() + " cannot be converted to Stream");
        }
    }
}
