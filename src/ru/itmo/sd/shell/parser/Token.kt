package ru.itmo.sd.shell.parser

enum class Token {
    CAT,    // cat
    ECHO,   // echo
    WC,     // wc
    PWD,    // pwd
    CD,    // cd
    LS,    // cd
    GREP,    // grep
    EXIT,   // exit
    TEXT,   // <some text>
    PIPE,   // |
    LET,    // let
    ASSIGN, // =
    END     // <end of input>
}
