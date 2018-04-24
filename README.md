## About

This Frankenstein's monster is a semi-system consisting in a script and a clojure program that should help perform the cluster analysis detailed in Adam Tornhill's excellent "Your Code as a Crime Scene".

After configuring the paths in the .sh file, you should in theory be able to generate the .json that you can use to replace ``hib_hotspot_proto.json`` in the hibernate demo to visualize your own data.

## Installation

This package requires codemaat to work, which can be cloned from here:

https://github.com/adamtornhill/code-maat.git

and the samples here

https://s3.amazonaws.com/CodeMaatDistro/sample0.2.zip

for the data visualization part (get the hybernate directory and replace the json).

You also need an http server, but 

    python -m SimpleHTTPServer 8080 

will probably do.

## License

This code is provided without warranty and free of charge under the GNU Public License v3.
