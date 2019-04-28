This is a ruby lib for handling data on the studio solution.

The goals are to be able to : 
- imports csv files (from SonicVisualizer, with timestamps and first locus)
- process data to quantize, get a "compass" track, get a "medioTiempo" track,  get the intersection beetween two tracks.
- export midi files and tempo change map (to use in ardour) 
- process midi files to json files.

# Execute UT

```
cd test
ruby -I . all.rb
```