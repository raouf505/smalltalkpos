
```
Gofer new    squeaksource: 'MetacelloRepository';    package: 'ConfigurationOfGlorpDBX';    load.

(ConfigurationOfGlorpDBX project version: '1.2') load: 'All with PostgreSQL native'.

Gofer new    squeaksource: 'MetacelloRepository';    package: 'ConfigurationOfSeaside28';    load.

(ConfigurationOfSeaside28 project version: '2.8.4.9') load.

Gofer new    squeaksource: 'MetacelloRepository';    package: 'ConfigurationOfMagritte';    load.

(ConfigurationOfMagritte project version: '1.2.1.4.1') load.

"Install Magritte-Seaside-lr.316 from Magritte-Tests on http://source.lukas-renggli.ch/magritte.html".

"Install SmallPOS from http://www.squeaksource.com/SmallPOS.html".

VzorSession dbServer: nil.

SmallPOSInterface initRoot: Menu session:VzorSession.
```

# ADDITIONAL STEPS UNDETECTED BY ARDUINO #

"Install SUExtensions (latest version I beleive, I used 42, and it was latest) from http://www.squeaksource.com/jQuery.html".