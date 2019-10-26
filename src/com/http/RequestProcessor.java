package com.http;

import java.io.IOException;

public interface RequestProcessor {
    String BAD_REQUEST = "Bad request (400).\n";
    String OK = "Ok (200).\n";

    String process() throws IOException;
}
