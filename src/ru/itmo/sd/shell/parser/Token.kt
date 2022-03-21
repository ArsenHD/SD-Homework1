package ru.itmo.sd.shell.parser

enum class Token {
    CAT,    // cat
    ECHO,   // echo
    WC,     // wc
    PWD,    // pwd
    EXIT,   // exit
    TEXT,   // <some text>
    PIPE,   // |
    LET,    // let
    DASH,   // -
    ASSIGN, // =
    END     // <end of input>
}
