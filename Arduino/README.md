# SIM Simulator

Oleh :

Oktavianus Handika / 13515035

Felix Limanta / 13515065

Rionaldi Chandraseta / 13515077

## Subsistem Arduino

Tujuan dari subsistem Arduino pada SIM Simulator adalah menyimulasikan pergerakan mo
bil virtual di subsistem Unity pada dunia nyata. Sebuah model mobil yang ditenagai Arduino
dapat dikendalikan melalui model mobil virtual di Unity untuk simulasi yang lebih nyata. 

Fitur yang ada pada subsistem Arduino ini :

- Deteksi Jarak

  Deteksi jarak pada model mobil menggunakan sensor ultrasonik. Data dari deteksi
  jarak ini akan dikirim ke LCD untuk ditampilkan nilainya.

- Deteksi Maju dan Mundur

  Deteksi pergerakan mobil apakah sedang maju, diam, atau mundur. Ketika maju, 7-*segment* akan menampilkan huruf 'd', ketika mundur akan menampilkan huruf 'r', dan 'p' ketika mobil berhenti.

- Bergerak

  Sistem dapat bergerak maju, mundur, dan berbelok sesuai dengan input *user* ketika dihubungkan dengan subsistem Unity. Mobil dapat bergerak menggunakan komponen motor untuk menggerakkan roda pada mobil tersebut.

Ketika *user* menggerakan mobil virtual di Unity, model mobil fisik Arduino juga akan ikut
bergerak mengikuti pergerakan mobil virtual tersebut. Sesuai dengan mobil virtual di Unity,
model mobil tersebut dapat bergerak maju dan mundur, serta dapat berbelok ke kiri dan ke
kanan sesuai pergerakan mobil yang dikendalikan di platform Unity. 