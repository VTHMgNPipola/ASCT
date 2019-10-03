# Advanced Structure Creation Tool

For the lack of a better name, I have choose one that pretty much
describes what is my aim with this "game".

## Introduction

In this game, inspired by The Powder Toy (referred as TPT in this
document), you can create electronic circuits that do all kinds of stuff
without all the hassle that creating such devices in real life have. But
it has several limitations.

Unlike TPT, the A.S.C.T. "world" is composed of layers. Each layer may
have a side of size x, and the layer will have a total area x^2.

The maximum size for an array in Java is the maximum integer value, that
is 2,147,483,647. To get the maximum size, you just need to get the integer
square root of that number, that is 46,340. A layer this big gives you
2,147,483,600 tiles to work with (more than 2 billion tiles). And that
for a single layer.

## Development

In its current state, you can do very little with the game.
I already discovered that a memory cell is possible for example
(and so should be a memory block, since it is just memory cells "glued"
together), but there isn't much more.

The game still have several problems, and some stuff that still should
be thought more, and I'll be trying to solve everything that I find.

## Modding

ASCT is developed using a library called
[ASCTlib](https://github.com/VTHMgNPipola/ASCTlib), that can be used to
mod ASCT.

Each ASCT mod should have a class that extends `ASCTMod`, found inside
ASCTlib. To add a new tile to the game, for example, you need to extend
the type of tile that you want to create (for example to create a basic
conductor create a class that extends `ConductorTile`) and add the
annotation `PlaceableTile`, and have a constructor with two integers (X
and Y position, in that order) as arguments or no arguments . ASCT will
recognize all classes that have that annotation and instantiate them by
those constructors.