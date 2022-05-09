package fr.dev.leaguacyapi.privates;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Secrets {
    SECRET("kj50jo3gmy9dbe5wt12u");

    private final String value;
};
