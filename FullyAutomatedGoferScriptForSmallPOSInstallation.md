
```
Gofer new    squeaksource: 'MetacelloRepository';    package: 'ConfigurationOfGlorpDBX';    load.
(ConfigurationOfGlorpDBX project latestVersion) load: 'GlorpDriverPostgreSQL'.

Gofer new    squeaksource: 'MetacelloRepository';    package: 'ConfigurationOfSeaside28';    load.
(ConfigurationOfSeaside28 project latestVersion) load.

Gofer new    squeaksource: 'MetacelloRepository';    package: 'ConfigurationOfMagritte';    load.
(ConfigurationOfMagritte project latestVersion) load.
(ConfigurationOfMagritte project latestVersion) load: 'Magritte-Seaside'.

(Gofer new)  squeaksource3: 'SmallPOS';	package: 'SmallPOS'; load.
(Gofer new)  squeaksource3: 'SmallPOS';	package: 'MoneyToWordsRus'; load.
	
(Gofer new) url: 'http://source.lukas-renggli.ch/unsorted'; package: 'RFB'; load.
(Gofer new) squeaksource3: 'SASExtensions'; package: 'pharoPatch'; load.
(Gofer new) squeaksource3: 'SASExtensions'; package: 'rfb-patch'; load.
```



```
"To init VNC-server"
RFBServer start.
RFBServer server setFullPassword:'123'.
```