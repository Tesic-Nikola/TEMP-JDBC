INSERT INTO Rudnik (Naziv, Lokacija, Datum_osnivanja, Pocetak_radnog_vremena, Kraj_radnog_vremena)
VALUES ('Rudnik Kolubara', 'Lazarevac', '1952-03-15', '06:00:00', '22:00:00');

INSERT INTO Rudnik (Naziv, Lokacija, Datum_osnivanja, Pocetak_radnog_vremena, Kraj_radnog_vremena)
VALUES ('Rudnik Tamnava', 'Ub', '1967-08-22', '05:30:00', '21:30:00');

INSERT INTO Rudnik (Naziv, Lokacija, Datum_osnivanja, Pocetak_radnog_vremena, Kraj_radnog_vremena)
VALUES ('Rudnik Kostolac', 'Kostolac', '1975-11-10', '07:00:00', '23:00:00');

INSERT INTO Rudnik (Naziv, Lokacija, Datum_osnivanja, Pocetak_radnog_vremena, Kraj_radnog_vremena)
VALUES ('Rudnik Djerdap', 'Kladovo', '1980-05-05', '06:30:00', '22:30:00');

INSERT INTO Rudnik (Naziv, Lokacija, Datum_osnivanja, Pocetak_radnog_vremena, Kraj_radnog_vremena)
VALUES ('Rudnik Bor', 'Bor', '1963-07-12', '05:00:00', '21:00:00');

-- Insert test data for Iskop table (Povrsina in hectares)

INSERT INTO Iskop (IskopID, Rudnik_RudnikID, Naziv, Datum_Pocetka, Povrsina)
VALUES (1, 1, 'Polje Tamnava-Zapadno Polje', '2020-06-01', 245);

INSERT INTO Iskop (IskopID, Rudnik_RudnikID, Naziv, Datum_Pocetka, Povrsina)
VALUES (2, 1, 'Polje D', '2019-09-15', 180);

INSERT INTO Iskop (IskopID, Rudnik_RudnikID, Naziv, Datum_Pocetka, Povrsina)
VALUES (3, 2, 'Polje Obrenovac', '2021-04-20', 320);

INSERT INTO Iskop (IskopID, Rudnik_RudnikID, Naziv, Datum_Pocetka, Povrsina)
VALUES (4, 2, 'Centralno Polje', '2018-11-10', 275);

INSERT INTO Iskop (IskopID, Rudnik_RudnikID, Naziv, Datum_Pocetka, Povrsina)
VALUES (5, 3, 'Drmno', '2022-02-25', 410);

INSERT INTO Iskop (IskopID, Rudnik_RudnikID, Naziv, Datum_Pocetka, Povrsina)
VALUES (6, 4, 'Sektor A', '2020-08-08', 195);

INSERT INTO Iskop (IskopID, Rudnik_RudnikID, Naziv, Datum_Pocetka, Povrsina)
VALUES (7, 5, 'Veliki Krivelj', '2019-01-14', 280);

-- Insert test data for Jaloviste table

INSERT INTO Jaloviste (JalovisteID, Status, Tip_materijala, Iskop_IskopID)
VALUES (1, 'Aktivno', 'Ugljeni mulj', 1);

INSERT INTO Jaloviste (JalovisteID, Status, Tip_materijala, Iskop_IskopID)
VALUES (2, 'Neaktivno', 'Pesak i glina', 2);

INSERT INTO Jaloviste (JalovisteID, Status, Tip_materijala, Iskop_IskopID)
VALUES (3, 'Aktivno', 'Jalovinski materijal', 3);

INSERT INTO Jaloviste (JalovisteID, Status, Tip_materijala, Iskop_IskopID)
VALUES (4, 'Sanacija', 'Glineni materijal', 4);

INSERT INTO Jaloviste (JalovisteID, Status, Tip_materijala, Iskop_IskopID)
VALUES (5, 'Aktivno', 'Flotacijska jalovista', 5);

INSERT INTO Jaloviste (JalovisteID, Status, Tip_materijala, Iskop_IskopID)
VALUES (6, 'Neaktivno', 'Kamen i zemlja', 6);

INSERT INTO Jaloviste (JalovisteID, Status, Tip_materijala, Iskop_IskopID)
VALUES (7, 'Aktivno', 'Rudna jalovista', 7);

-- Insert test data for Radna_Lok table

INSERT INTO Radna_Lok (RadLokID, Naziv, Status, Aktivnosti, Opasnosti, Iskop_IskopID, Radna_Lok_RadLokID)
VALUES (1, 'Sektor 1 - Glavna etaza', 'Aktivna', 'Iskop uglja', 'Klizanje terena', 1, NULL);

INSERT INTO Radna_Lok (RadLokID, Naziv, Status, Aktivnosti, Opasnosti, Iskop_IskopID, Radna_Lok_RadLokID)
VALUES (2, 'Sektor 2 - Donja etaza', 'Aktivna', 'Transport materijala', 'Padanje stena', 1, 1);

INSERT INTO Radna_Lok (RadLokID, Naziv, Status, Aktivnosti, Opasnosti, Iskop_IskopID, Radna_Lok_RadLokID)
VALUES (3, 'Sektor 3 - Prilazni put', 'Neaktivna', 'Priprema terena', 'Pad sa visine', 2, NULL);

INSERT INTO Radna_Lok (RadLokID, Naziv, Status, Aktivnosti, Opasnosti, Iskop_IskopID, Radna_Lok_RadLokID)
VALUES (4, 'Centralna rampa', 'Aktivna', 'Utovar i istovar', 'Saobraćajna nezgoda', 3, NULL);

INSERT INTO Radna_Lok (RadLokID, Naziv, Status, Aktivnosti, Opasnosti, Iskop_IskopID, Radna_Lok_RadLokID)
VALUES (5, 'Pomoćni sektor', 'Aktivna', 'Održavanje opreme', 'Mehanička povreda', 3, 4);

INSERT INTO Radna_Lok (RadLokID, Naziv, Status, Aktivnosti, Opasnosti, Iskop_IskopID, Radna_Lok_RadLokID)
VALUES (6, 'Odlagalište A', 'Aktivna', 'Odlaganje jalovine', 'Prašina i dim', 4, NULL);

INSERT INTO Radna_Lok (RadLokID, Naziv, Status, Aktivnosti, Opasnosti, Iskop_IskopID, Radna_Lok_RadLokID)
VALUES (7, 'Kontrolna stanica', 'Aktivna', 'Monitoring rada', 'Niska vidljivost', 5, NULL);

INSERT INTO Radna_Lok (RadLokID, Naziv, Status, Aktivnosti, Opasnosti, Iskop_IskopID, Radna_Lok_RadLokID)
VALUES (8, 'Servisni centar', 'Aktivna', 'Popravka mašina', 'Hemijska izloženost', 6, NULL);

INSERT INTO Radna_Lok (RadLokID, Naziv, Status, Aktivnosti, Opasnosti, Iskop_IskopID, Radna_Lok_RadLokID)
VALUES (9, 'Flotacijski pogon', 'Aktivna', 'Obrada rude', 'Toksični gasovi', 7, NULL);

-- Insert test data for Masina table (Kapacitet in KG)

INSERT INTO Masina (MasinaID, Rudnik_RudnikID, Naziv, Tip, Status, Godina_proizvodnje, Kapacitet)
VALUES (1, 1, 'Bager SRS-1200', 'Kontinuirani bager', 'U radu', 2018, 120000);

INSERT INTO Masina (MasinaID, Rudnik_RudnikID, Naziv, Tip, Status, Godina_proizvodnje, Kapacitet)
VALUES (2, 1, 'Transporter T-500', 'Transportni sistem', 'U radu', 2019, 50000);

INSERT INTO Masina (MasinaID, Rudnik_RudnikID, Naziv, Tip, Status, Godina_proizvodnje, Kapacitet)
VALUES (3, 2, 'Kamion Volvo FH16', 'Utovarivač', 'Servis', 2020, 40000);

INSERT INTO Masina (MasinaID, Rudnik_RudnikID, Naziv, Tip, Status, Godina_proizvodnje, Kapacitet)
VALUES (4, 2, 'Dozzer CAT D11', 'Dozzer', 'U radu', 2017, 85000);

INSERT INTO Masina (MasinaID, Rudnik_RudnikID, Naziv, Tip, Status, Godina_proizvodnje, Kapacitet)
VALUES (5, 3, 'Hidraulični bager EX-8000', 'Hidraulični bager', 'U radu', 2021, 80000);

INSERT INTO Masina (MasinaID, Rudnik_RudnikID, Naziv, Tip, Status, Godina_proizvodnje, Kapacitet)
VALUES (6, 3, 'Kamion Komatsu 980E', 'Rudarski kamion', 'Neispravan', 2016, 320000);

INSERT INTO Masina (MasinaID, Rudnik_RudnikID, Naziv, Tip, Status, Godina_proizvodnje, Kapacitet)
VALUES (7, 4, 'Drobilica K-300', 'Primarna drobilica', 'U radu', 2019, 30000);

INSERT INTO Masina (MasinaID, Rudnik_RudnikID, Naziv, Tip, Status, Godina_proizvodnje, Kapacitet)
VALUES (8, 5, 'Flotacijska jedinica F-150', 'Flotacijski separator', 'Servis', 2020, 15000);

INSERT INTO Masina (MasinaID, Rudnik_RudnikID, Naziv, Tip, Status, Godina_proizvodnje, Kapacitet)
VALUES (9, 5, 'Konvejer KC-2000', 'Transportni konvejer', 'U radu', 2018, 200000);

INSERT INTO Masina (MasinaID, Rudnik_RudnikID, Naziv, Tip, Status, Godina_proizvodnje, Kapacitet)
VALUES (10, 1, 'Kompresori K-500', 'Pneumatski kompresor', 'U radu', 2022, 50000);

COMMIT;