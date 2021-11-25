# Gamermode

## Design choices

- No custom NBT implementation
    - In the first version of Gamermode, I wrote
      a [custom NBT implementation](https://github.com/Floffah/gamermode/tree/new/src/main/java/dev/floffah/gamermode/nbt)
      however it was unstable and too bloated. Instead, I opted to use something that people are more familiar with (
      Querz's implementation)
- No custom components implementation
    - This was mainly motivated by laziness and that I thought it might be better to use something more fleshed out,
      stable, and known in the community and ended up going with KyoriPowered's Adventure