package com.claudechrist.premiere_soccer_league_app;

public class MatchComment {

    private Match match;
    private String id;
    private String comment;

    public MatchComment() {
    }

    public MatchComment(Match match, String comment) {
        this.match = match;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
