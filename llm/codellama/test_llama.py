from langchain.callbacks.manager import CallbackManager
from langchain.callbacks.streaming_stdout import StreamingStdOutCallbackHandler
from langchain.chains import LLMChain
from langchain.llms import LlamaCpp
from langchain.prompts import PromptTemplate

template = """Given a comma separated list of fields, generate 100 lines of synthetic data for each field in json format.
Fields:{fields}
"""

prompt = PromptTemplate(template=template, input_variables=["fields"])

callback_manager = CallbackManager([StreamingStdOutCallbackHandler()])

n_gpu_layers = 40  # Change this value based on your model and your GPU VRAM pool.
n_batch = 512  # Should be between 1 and n_ctx, consider the amount of VRAM in your GPU.

# Make sure the model path is correct for your system!
llm = LlamaCpp(
    model_path="../models/llama-7B/llama-2-7b.Q6_K.gguf",
    n_gpu_layers=n_gpu_layers,
    n_batch=n_batch,
    callback_manager=callback_manager,
    verbose=True,  # Verbose is required to pass to the callback manager
)


llm_chain = LLMChain(prompt=prompt, llm=llm)
fields = "name,surname,email"
llm_chain.run(fields=fields)

