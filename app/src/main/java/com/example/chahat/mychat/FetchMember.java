package com.example.chahat.mychat;

/**
 * Created by chahat on 24/3/16.
 */
interface FetchMember {

    /**
     * Invoked when background task is completed
     */

    public abstract void done(GroupChat returnedMember);
}
