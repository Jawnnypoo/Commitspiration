package com.commit451.commitspiration.model;

/**
 * All the data for a WhatTheCommit message
 */
public class WhatTheCommitData {

    public final String message;
    public final String url;

    public WhatTheCommitData(String message, String url) {
        this.message = message;
        this.url = url;
    }
}
