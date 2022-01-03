BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "korisnik" (
	"ime"	TEXT,
	"prezime"	TEXT,
	"email"	TEXT,
	"username"	TEXT,
	"password"	TEXT,
	"id"	TEXT,
	PRIMARY KEY("id")
);
INSERT INTO "korisnik" VALUES ('Vedran','Ljubović','vljubovic@etf.unsa.ba','vedranlj','test','1');
INSERT INTO "korisnik" VALUES ('Amra','Delić','adelic@etf.unsa.ba','amrad','test','2');
INSERT INTO "korisnik" VALUES ('Tarik','Sijerčić','tsijercic1@etf.unsa.ba','tariks','test','3');
INSERT INTO "korisnik" VALUES ('Rijad','Fejzić','rfejzic1@etf.unsa.ba','rijadf','test','4');
COMMIT;
