# Advanced Structure Creation Tool

For the lack of a better name, I have choose one that pretty much
describes what is my aim with this "game".

## Introduction

In this game, inspired by The Powder Toy (referred as TPT in this
document), you can create electronic circuits that do all kinds of
stuff, but without all the hassle that creating such devices in real
life have.

Unlike TPT, the A.S.C.T. "world" is composed of layers. Each layer may
have a size of 512x512, 1024x1024, 2048x2048, and even more tiles.

If you allocate enough RAM for it, a maximum theoretical size of more
than 32768x32768 can be reached, though I never could test that (my
computer does not have enough RAM). That's more than *1 **billion**
tiles **per layer***!   

## Development

In its current state, you can do very little with the game.
I already discovered that a memory cell is possible for example
(and so should be a memory block, since it is just memory cells "glued"
together), but there isn't much more.

I will create a Java library that will accommodate the Tile class
and the basic structure around it, so that modding is possible.