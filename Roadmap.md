# [i18n](http://en.wikipedia.org/wiki/I18n) #
Some object **SPLocale** should be defined (or maybe some existing may be used), which should hold information (list may be filled up):
  * language ISO code (for using with )
  * months names
  * alhorithm of string representation of currency (and, maybe, any number)
  * currency name (ISO code, prefix, postfix)
  * applicable decimal points symbols, and prefferable one

I think, some **SPTranslation** object should be defined, with translation of strings like "Save", "Exit", "Generate" and so on. It should hold language ISO code aswell. It should be separated from **SPLocale** to have a possibility to make several translation versions for one language (for example, it may be "Generate" or "Generate report" or "Submit").

SPSession should hold one instance of **SPLocale** and one instance of **SPTranslation**. And every component should/may ask sesion for necessary locale info.

# Reports engine #
Now report engine supports only simple "flat" reports.
I also need
  * grouping report
  * pivot table report
and, maybe, special separate
  * turnover balance report
and, maybe again, special separate
  * goods cost report (maybe it will be three different reports: for FIFO method, for average method and for last bought price method)

# Updating to contemporary technologies #
## Port to Seaside 3.0 ##
For static files hosting via comanche with seaside3 (which I stacked in) see http://samadhiweb.com/blog/2011.07.10.seaside.comanche.staticfiles.html
## Port to DBXTalk version of GLORP ##
They ported GLORP from VW (I don't know if it lastone or not), and it works with MySQL aswell. See http://lists.gforge.inria.fr/pipermail/pharo-project/2011-June/049978.html for example
## Prepare "installer" ##
Make a Gofer ruleset(s) no make new images production fast and easy. Maybe http://www.squeaksource.com/Installer.html should be also used.