# altoclef-example
Example Repository for making custom altoclef commands and tasks

# How to Develop/Use

1) Copy this repo/make a template repository
2) If you are using an IDE and its linting isn't working, run `gradle build` once (it should download altoclef dependencies and kick in from there)
3) run `gradle runClient`. Load into a world. Send the example command to verify the repo works (`@example`)
4) Use the examples to add your own custom commands and tasks.


# Task Development Stream: Common Edge cases/bugs to keep in mind!

1) Always implement `isEqual`!
2) Whenever going to the closest object, remember the ping pong movement issue (to approach one goal it gets closer to another). This can be fixed in one of the following ways:
    - Committing to one (`_target = closest`, `if (target != null) {do to closest}`)
    - Use `DoToClosestBlockTask`/`DoToClosestEntityTask`
3) What if your target is surrounded in bedrock or is floating way too high up in the air? If a block is unreachable/the bot fails to get there: Use `mod.getBlockTracker().unreachable(blockpos)`


# Coming Soon...
- Commands with arguments examples
- A task demonstrating each system that you may use with the bot.
