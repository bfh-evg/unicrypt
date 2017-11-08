UniCrypt
========
UniCrypt is a Mathematical Crypto-Library for Java.

UniCrypt(tm): Cryptographic framework allowing the implementation of
cryptographic protocols, e.g. e-voting.

## Randomness
To speed up HybridRandomByteSequence under linux install rng-tools.
Can be verified by cat /dev/random.

## Design Principles
- Full coherence with mathematical and cryptographic concepts 
- Consistent and self-explanatory nomenclature
- Clean and intuitive APIs
- Generic types
- Consistent coding style
- Immutable objects only
- Design patterns
- No cryptographic black-boxes(e.g. random generator) 
- Java 7 compatibility

## Architecture
The System Architecture consists of three layers:

<img width="941" alt="screen shot 2017-11-07 at 4 58 53 pm" src="https://user-images.githubusercontent.com/15310211/32497291-1ef893f2-c3dd-11e7-8623-a8001b648be4.png">



- Layer 0 (Utils):

This layer has a collection of utility functions which are:
1) Helper
this includes (converter, aggregator, immutable array, byte array, alphabet, permutation, point, polynomial, tree, ...)
2) Random

- Layer 1 (Mathmatics) :

1) It includes Algebra package with different algebraic structures (set, monoid, semigroup, group, semiring, ring, field, finite field, prime field, polynomial field, elliptic curves, product set, N, Z, Zn, Z*n, G*q, ...).
2) It also includes mathematical functions, There are a large set of predefined functions such as AdapterFunction, AdditionFunction, ConstantFunction, ConvertFunction, EqualityFunction, HashFunction, IdentityFunction, InvertFunction, ModuloFunction, MultiplicationFunction, PermutationFunction, PowerFunction, SelectionFunction.


