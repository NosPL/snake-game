package com.noscompany.message.publisher;

import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class GenericTypeUnwrapperTest {
    private final GenericTypeUnwrapper genericTypeUnwrapper = new GenericTypeUnwrapper("other/tests");

    @Test
    public void test() {
        assertEquals(Option.none(), genericTypeUnwrapper.unwrap(null));
        assertEquals(Option.none(), genericTypeUnwrapper.unwrap(VoidResult.VOID_RESULT));
        assertEquals(Option.of(new Result()), genericTypeUnwrapper.unwrap(Option.of(new Result())));
        assertEquals(Option.of(new Result()), genericTypeUnwrapper.unwrap(Optional.of(new Result())));
        assertEquals(Option.of(new Result()), genericTypeUnwrapper.unwrap(Either.left(new Result())));
        assertEquals(Option.of(new Result()), genericTypeUnwrapper.unwrap(Either.right(new Result())));
        assertEquals(Option.of(new Result()), genericTypeUnwrapper.unwrap(Try.success(new Result())));

        var tryException = new TryException();
        assertEquals(Option.of(tryException), genericTypeUnwrapper.unwrap(Try.failure(tryException)));

        var mess = Try.success(Either.left(Option.of(Either.right(Optional.of(new Result())))));
        assertEquals(Option.of(new Result()), genericTypeUnwrapper.unwrap(mess));
    }
}