package com.marcus.fullcycle.video.catalog.admin.application;

public abstract class UseCase<Input, Output> {
    public abstract Output execute(Input input);
}
