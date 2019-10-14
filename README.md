# bayes-spam-filter

**Contributors**
* Marc Sieber
* Steve Vogel
* Mike Nöthiger

**How To Run**  
`Main` Klasse als Java Programm starten. Dann wird:
1. Kallibrierungswerte gesetzt, die sich aus unserer Analyse ergeben haben (es gibt auch die `autoCalibrate()` methode, die durch Versuch und Irrtum die besten Werte ermittelt).
2. Mittels Anlern-Emails eine Analyse erstellt, die dazu dient, mails richtig zu klassifizieren
3. Die Test-Emails klassifiziert sowie eine Auswertung über die Erkennungsrate und Schwellwerte ausgegeben.

**Interessante Methoden**
* `BayesSpamFilter.learn()`
* `BayesSpamFilter.calcProbability()`
* `BayesSpamFilter.runTest()`
* `BayesSpamFilter.autoCalibrate()`

**Begründung 2b)**  
Eine Spam- oder Hamwahrscheinlichkeit von Null bei einem Wort, führt dazu, dass die Gesamtwahrscheinlichkeit zerstört wird, da diese Null als Faktor in der Berechnungsformel auftaucht.