# Evently

Backend service voor het aanmaken van dynamische inschrijfformulieren en het opslaan van inzendingen.

## Gebouwd met

- Java 21
- Spring Boot
- Maven
- PostgreSQL
- Docker / Docker Compose

## Starten

Vereiste: Docker moet geïnstalleerd en gestart zijn.

Start de volledige applicatie met één commando:

```bash
docker compose up --build
```

De API is daarna beschikbaar op:

```text
http://localhost:8080
```

PostgreSQL draait op poort `5432`.

## Functionaliteit

- Formulieren aanmaken
- Formulieren ophalen
- Inzendingen opslaan per formulier
- Inzendingen ophalen per formulier
- Validatie van verplichte velden en veldtypes
- Duidelijke foutmeldingen bij ongeldige input

Ondersteunde veldtypes:

```text
TEXT
EMAIL
NUMBER
DATE
CHOICE
```

## API

### Formulier aanmaken

```http
POST http://localhost:8080/api/forms
```

```json
{
  "name": "Borrel event",
  "fields": [
    {
      "name": "fullName",
      "type": "TEXT",
      "required": true,
      "options": []
    },
    {
      "name": "email",
      "type": "EMAIL",
      "required": true,
      "options": []
    },
    {
      "name": "experience",
      "type": "CHOICE",
      "required": true,
      "options": [
        "BEGINNER",
        "INTERMEDIATE",
        "ADVANCED"
      ]
    }
  ]
}
```

### Alle formulieren ophalen

```http
GET http://localhost:8080/api/forms
```

### Formulier ophalen

```http
GET http://localhost:8080/api/forms/{formId}
```

### Inzending opslaan

```http
POST http://localhost:8080/api/forms/{formId}/submissions
```


```json
{
  "answers": {
    "fullName": "Ahmed",
    "email": "ahmed@example.com",
    "experience": "BEGINNER"
  }
}
```

### Inzendingen van een formulier ophalen

```http
GET http://localhost:8080/api/forms/{formId}/submissions
```




