# Build

```sh
$ make everything
```

That is it - that will build a version of the app that you can profile, it will run it and hit the endpoints, kill the app, then build
a native binary.

## Notes

Look out for the use of a system property to change the AOT mode flag for Spring.
It seems you need to o the following (and this automates it):

1. Build for profiling with the agent : `mode = AotMode.NATIVE_AGENT`
2. Profile to capture reflection config etc.
3. Build a native binary: `mode = AotMode.NATIVE`

If I have this wrong, please do raise a PR :)