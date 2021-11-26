# Gamermode

## Contributing

When contributing please make sure at the very least you have nodejs and yarn installed and have ran `yarn prepare` in
this repo to install the git hooks. This is because we try and keep all of the code pretty using prettier and intellij's
prettier plugin doesn't support java files even when added to the regex.

## Design choices

- No custom NBT implementation
    - In the first version of Gamermode, I wrote
      a [custom NBT implementation](https://github.com/Floffah/gamermode/tree/new/src/main/java/dev/floffah/gamermode/nbt)
      however it was unstable and too bloated. Instead, I opted to use something that people are more familiar with (
      Querz's implementation)
- No custom components implementation
    - This was mainly motivated by laziness and that I thought it might be better to use something more fleshed out,
      stable, and known in the community and ended up going with KyoriPowered's Adventure