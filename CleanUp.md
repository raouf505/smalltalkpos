
```
"kill seaside processes."
WAKom stop.

"make sure they're dead"
HttpService allInstances do: [:x | x stop].

"clears cached data"
WALRUCache allInstances do: [:x | x initialize].

"clears context data"
WARenderingContext allInstances do: [:x | x clearMode].

"expires sessions"
WASession allInstances do: [:x | x expire].

"cleaning sessions as it described in WASession description"
WARegistry clearAllHandlers.

"default garbage collection - do this several times just to be sure"
Smalltalk garbageCollect.
```