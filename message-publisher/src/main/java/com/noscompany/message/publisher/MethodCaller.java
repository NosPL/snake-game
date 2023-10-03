package com.noscompany.message.publisher;

import lombok.Value;

@Value
class MethodCaller {
    Exception stackTrace = new Exception();
    String threadName = Thread.currentThread().getName();
}