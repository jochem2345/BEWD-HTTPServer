# Introductie

Deze oefening is deel van de BEWD Course aan de Hogeschool Arnhem/Nijmegen.
Onderwerp is het bekend raken met het Http-protocol in een simpele Java-server.

Web servers, gebruikt op het internet zijn feitelijk server-software die voldoen
aan het Http-protocol.
Het Http-protocol, zoals ook alle andere internet protocols, is gespecificeerd
in een *Request for Comments* (RFC). De RFC voor Http kan hier gevonden
worden: [https://tools.ietf.org/html/rfc7230]().

# Opdrachten voor de voorbereiding

Bekijk de startcode in de master-branch. De server is op te starten via de
main-methode van de `HttpServer` klasse.

## 1) Een Request sturen via de browser

Voer deze main-methode van de `HttpServer` klasse uit, zodat de Server wordt
opgestart. Navigeer via je browser naar [http://localhost:8383/index.html]() om
te zien wat de server teruggeeft. 

Je hebt nu via je browser een HTTP-request uitgevoerd. Watvoor request?

## 2) De log in de console

Bekijk de console van je IDE. Wat zie je hier precies terug?

## 2) Helder krijgen hoe de HTTP-server werkt

Om helder te krijgen hoe de HTTP-server werkt, maken we gebruik van het
geheugenmodel. Hieronder het geheugenmodel op het moment dat `accept()`
wordt aangeroepen op de instantie van `ServerSocket`.

![Geheugen Model 1](images/mm_accept.png)

* Teken nu een nieuw geheugenmodel voor het moment dat de
  `ConnectionHandler` is aangemaakt en ook daar `accept()` op wordt
  aangeroepen, maar hij nog niet in deze methode is gegaan.
* Wanneer is het Http-request nu volledig afgehandeld? Op welke regel code
  zit je dan?

## 3) Het vinden van de oorzaak van een onvolledig HTML-bestand in de response

Misschien heb je het al opgemerkt, maar het HTML-bestand dat door de server
wordt teruggegeven, wordt niet volledig getoond in de browser. Het stuk:

```html
<p>This is another simple line in html.</p>
</body>
</html>
```

is niet zichtbaar. Dit komt doordat er nog iets fout staat in de headers van
het HTTP-response. Bekijk de console van je IDE, waarin ook de headers van
de response zijn terug te lezen.
Welk onderdeel van de header zou de fout veroorzaken?

## 4) Het oplossen van de oorzaak van een onvolledig HTML-bestand in de response

De `ConnectionHandler` bevat al de benodigde methode om het probleem op te
lossen. Gebruik deze om het probleem op te lossen.

# Opdrachten voor de les

## 5) Gebruik Postman om een Request te sturen

Gebruik nu Postman om een GET-request te sturen naar je eigen Http-server.

* Verstuur een GET naar URL de [http://localhost:8383/index.html]() en bekijk
  het resultaat.
* Selecteer nu een andere Http-methode in Postman en bekijk het resultaat.
  Is dit wat je verwacht? En kun je in de Java code de plek aanwijzen waar
  dit is uitgeprogrammeerd?

## 6) Afhandelen van een POST-methode

Pas de code van de Http-Server nu zo aan dat ook een POST wordt geaccepteerd 
en correct wordt afgehandeld.

* Na een POST-Request moet de Http-server de HTTP-statuscode voor CREATED 
  terugsturen.
* De Body van het HTTP-Request moet naar de console worden geprint.













 



