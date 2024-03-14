package com.marcus.fullcycle.video.catalog.admin.application;

public abstract class InputOnlyUseCase<Input> {
    public abstract void execute(Input input);
}
