# Advanced Structure Creation Tool

For the lack of a better name, I have choose one that pretty much
describes what is my aim with this "game".

## Introduction

In this game, inspired by The Powder Toy (referred as TPT in this
document), you can create electronic circuits that do all kinds of
stuff, but without all the hassle that creating such devices in real
life have. But it has several limitations.

Unlike TPT, the A.S.C.T. "world" is composed of layers. Each layer may
have a size x, and the layer will have size x^2.

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

I will soon create a Java library that will accommodate the Tile class
and the basic structure around it, so that modding is possible.