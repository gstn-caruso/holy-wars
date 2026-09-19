package holywars;

/**
 * A single unit of application behavior that domain exports to its callers.
 *
 * @param <I> the input the use case needs to run
 * @param <O> the output the use case produces
 */
public interface UseCase<I, O> {

    /**
     * Runs the use case for the given input and returns its output.
     *
     * @param input the input the use case needs to run
     * @return the output the use case produces
     */
    O run(I input);
}
