BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "korisnik" (
	"ime"	TEXT,
	"prezime"	TEXT,
	"email"	TEXT,
	"username"	TEXT,
	"password"	TEXT
);
INSERT INTO "korisnik" VALUES ('Vedran','Ljubović','vljubovic@etf.unsa.ba','vedranlj','test');
INSERT INTO "korisnik" VALUES ('Amra','Delić','adelic@etf.unsa.ba','amrad','test');
INSERT INTO "korisnik" VALUES ('Tarik','Sijerčić','tsijercic1@etf.unsa.ba','tariks','test');
INSERT INTO "korisnik" VALUES ('Rijad','Fejzić','rfejzic1@etf.unsa.ba','rijadf','test');
COMMIT;
