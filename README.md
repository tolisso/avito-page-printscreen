Working ONLY on linux

## About

This program print avito's housing page with opened phone number.

There is also a garbage in the stderr, so you can redirect it to
`/dev/null` with: 

`#command_starts_program# 2> /dev/null`

## Requirements

You need to have **chrome** and **selenium plugin to chrome**.

## Build

To build project you need to add additional modules to it. 
They are locating in packages

*plugins/selenium-java-@version* and 

*plugins/selenium-java-@version/libs*

## How to use

Launch with arguments: (link), (output-file), (output-file-extension)

(link) must point to page with one housing with number

