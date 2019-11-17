# Java Exam for the sophisticated

[![Build Status](https://travis-ci.com/Westerdals/pgr203-2019-eksamen-SeventhDisaster.svg?token=ANpvyaXvsPqVgppAsgx4&branch=master)](https://travis-ci.com/Westerdals/pgr203-2019-eksamen-SeventhDisaster)

Alternativ Travis Link: https://travis-ci.com/Westerdals/pgr203-2019-eksamen-SeventhDisaster

This project was made as a pair-programming exam by: Krister Emanuelsen (SeventhDisaster) and Tommy Hamarsnes (tommyhama95) 
## Hvordan kjøre dette programmet

### Bygg og test executable jar-fil

1. Kjør `mvn clean` først for å rense /target, deretter kjør `mvn package` for å opprette .jar filen som skal kjøres.
2. Konfigurasjonsfilen må hete `task-manager.properties` og må inneholde følgende:
    * `dataSource.url = jdbc:postgresql://localhost:5432/` + og navnet på din database.
    * `dataSource.username = ` brukernavn til database-eier.
    * `dataSource.password = ` passord til brukeren.
3. Etter du har kjørt `mvn package` kan du kjøre serveren fra target undermappen ved å benytte: `java -jar target/task-manager-1.0-SNAPSHOT.jar`
    - Dersom dette ikke går, pass på at navnet på jar filen stemmer innenfor /target mappen.

### Funksjonalitet

1. Programmet kan interageres med i nettleser ved å besøke http://localhost:8080
2. Dersom du skriver `/echo?` etter url'en vil du kunne utføre generelle Echo forespørsler mot serveren, slik du kan på steder som URLecho.
3. Ved å trykke på `Add/Create new x` knappene på nettsiden, vil du kunne fylle inn skjemaer for å legge til nye elementer i databasen.
4. Når du trykker på lenkene til hver bruker, blir du tatt til et skjema der du får muligheten til å `endre navn og epost til bruker`.
5. Du kan besøke prosjekter ved å trykke på lenkene. Her har du muligheten til å `Endre på prosjektnavnet`, `Legge til nye oppgaver` og også `Legge til nye prosjektmedlemmer til prosjektet du er inni`
6. Ved å trykke på brukere **som er en del av prosjektet** vil du kunne `filtrere prosjektoppgaver` på brukeren du velger.
7. Ved å trykke på oppgavene i prosjektet vil du bli tatt til en detaljeside om oppgaven. Der vil du se `Oppgavestatus`, `Tildelte medlemmer` og `Mulighet til å endre på oppgavenavn`
8. Du kan trykke på oppgavestatusen for å `endre på oppgavestatus fritt`
9. Innlegg til databasen via POST requests vil også re-directe deg tilbake til den mest relevante siden.
10. Alle `tilbake` lenker tar deg tilbake til forrige ledd i prosessen

## Designbeskrivelse

#### Datamodel
![Design](./Docs/Datamodell.png)

#### HTTPServer Diagram
![Design](./Docs/HTTP.png)



## Egenevaluering
We feel that we've had a very good teamwork in this exam all the way trough. 
We have only been working when we have been together in physical meet ups and mostly debugged when alone at certain
points in the process. 

Has been a really fun and interesting exam!

### Hva vi lærte underveis

### Hva vi fikk til bra i koden vår

### Hva vi skulle ønske vi hadde gjort annerledes

## Evaluering fra annen gruppe

## Evaluering gitt til annen gruppe
