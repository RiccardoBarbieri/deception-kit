from langchain.callbacks.manager import CallbackManager
from langchain.callbacks.streaming_stdout import StreamingStdOutCallbackHandler
from langchain.llms import LlamaCpp
from langchain.prompts import FewShotPromptTemplate, PromptTemplate
from langchain.pydantic_v1 import BaseModel
from langchain_experimental.tabular_synthetic_data.base import SyntheticDataGenerator
from langchain_experimental.tabular_synthetic_data.openai import (
    create_openai_data_generator,
    OPENAI_TEMPLATE,
)
from langchain_experimental.tabular_synthetic_data.prompts import (
    SYNTHETIC_FEW_SHOT_PREFIX,
    SYNTHETIC_FEW_SHOT_SUFFIX
)

from typing import (
    Any,
    Callable,
    Dict,
    List,
    Optional,
    Sequence,
    Tuple,
    Type,
    Union,
    cast,
)

from langchain.base_language import BaseLanguageModel
from langchain.chains import LLMChain
from langchain.output_parsers.openai_functions import (
    JsonOutputFunctionsParser,
    PydanticAttrOutputFunctionsParser,
    PydanticOutputFunctionsParser,
)
from langchain.prompts import BasePromptTemplate
from langchain.pydantic_v1 import BaseModel
from langchain.schema import BaseLLMOutputParser
from langchain.schema.output_parser import BaseGenerationOutputParser, BaseOutputParser
from langchain.schema.runnable import Runnable
from langchain.utils.openai_functions import convert_pydantic_to_openai_function


def create_fn_chain(
    # functions: Sequence[Union[Dict[str, Any], Type[BaseModel], Callable]],
    llm: BaseLanguageModel,
    prompt: BasePromptTemplate,
    *,
    output_key: str = "function",
    output_parser: Optional[BaseLLMOutputParser] = None,
    **kwargs: Any,
) -> LLMChain:
    # if not functions:
    #     raise ValueError("Need to pass in at least one function. Received zero.")
    # # openai_functions = [convert_to_openai_function(f) for f in functions]
    # # output_parser = output_parser or get_openai_output_parser(functions)
    # llm_kwargs: Dict[str, Any] = {
    #     "functions": functions,
    # }
    # if len(functions) == 1:
    #     llm_kwargs["function_call"] = {"name": functions[0]["name"]}
    llm_chain = LLMChain(
        llm=llm,
        prompt=prompt,
        output_parser=output_parser,
        # llm_kwargs=llm_kwargs,
        output_key=output_key,
        **kwargs,
    )
    return llm_chain


def create_structured_output_chain(
    output_schema: Union[Dict[str, Any], Type[BaseModel]],
    llm: BaseLanguageModel,
    prompt: BasePromptTemplate,
    *,
    output_key: str = "function",
    output_parser: Optional[BaseLLMOutputParser] = None,
    **kwargs: Any,
) -> LLMChain:
    if isinstance(output_schema, dict):
        function: Any = {
            "name": "output_formatter",
            "description": (
                "Output formatter. Should always be used to format your response to the"
                " user."
            ),
            "parameters": output_schema,
        }
    else:

        class _OutputFormatter(BaseModel):
            """Output formatter. Should always be used to format your response to the user."""  # noqa: E501

            output: output_schema  # type: ignore

        function = _OutputFormatter
        output_parser = output_parser or PydanticAttrOutputFunctionsParser(
            pydantic_schema=_OutputFormatter, attr_name="output"
        )
    return create_fn_chain(
        # [function],
        llm,
        prompt,
        output_key=output_key,
        output_parser=output_parser,
        **kwargs,
    )


def create_data_generator(
    output_schema: Union[Dict[str, Any], Type[BaseModel]],
    llm: LlamaCpp,
    prompt: BasePromptTemplate,
    output_parser: Optional[BaseLLMOutputParser] = None,
    **kwargs: Any,
) -> SyntheticDataGenerator:
    # Create function calling chain to ensure structured output
    chain = create_structured_output_chain(
        output_schema, llm, prompt, output_parser=output_parser, **kwargs
    )

    # Create the SyntheticDataGenerator instance with the created chain
    generator = SyntheticDataGenerator(template=prompt, llm_chain=chain)
    return generator


class User(BaseModel):
    name: str
    surname: str
    email: str


examples = [
    {
        "example": "Name: John, Surname: Doe, Email: john.doe@corporate.com"
    },
    {
        "example": "Name: Daniel, Surname: Smith, Email: daniel.smith@corporate.com"
    },
    {
        "example": "Name: Jane, Surname: Dove, Email: jane.doe@corporate.com"
    }
]

TEMPLATE = PromptTemplate(
    input_variables=["example"],
    template="{example}",
)

prompt_template = FewShotPromptTemplate(
    prefix=SYNTHETIC_FEW_SHOT_PREFIX,
    examples=examples,
    suffix=SYNTHETIC_FEW_SHOT_SUFFIX,
    input_variables=["subject", "extra"],
    example_prompt=TEMPLATE,
)

callback_manager = CallbackManager([StreamingStdOutCallbackHandler()])

# Initializes the synthetic data generator with the defined schema and language model, ready to generate data.
synthetic_data_generator = create_openai_data_generator(
    output_schema=User,
    llm=LlamaCpp(
        model_path="../models/codellama-7B/ggml-mode-q4_0.bin",
        temperature=0.75,
        max_tokens=2000,
        top_p=1,
        callback_manager=callback_manager,
        verbose=True,  # Verbose is required to pass to the callback manager
    ),  # Note: Replace with actual Language Model instance with your API key
    prompt=prompt_template,
)

# Generates synthetic data based on the input subject and additional criteria for naming.
synthetic_results = synthetic_data_generator.generate(
    subject="corporate_user",
    extra="the name must be chosen at random. Make it something you wouldn't normally choose.",
    runs=10,
)

# Outputs the synthetic data generated by the model.
print(synthetic_results)

