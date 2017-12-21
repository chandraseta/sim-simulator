# SIM Simulator

Oleh :

Oktavianus Handika / 13515035

Felix Limanta / 13515065

Rionaldi Chandraseta / 13515077



## Subsistem Unity

Sistem menyimulasikan ujian praktik untuk pengambilan SIM A. Ketika *user* ingin melakukan ujian praktik, sistem akan memeriksa terlebih dahulu apakah nilai ujian teori *user* mencukupi. Hanya *user* yang lulus ujian teori yang dapat mengambil ujian praktik dalam Unity. Kemajuan *user* akan dicatat dan dimasukkan ke dalam *cloud server*. 

*User* dapat mengemudikan sebuah mobil pada aplikasi ini. Dalam aplikasi, tersedia beberapa *level* berbeda di mana *user* harus berhasil mengendalikan mobil melalui berbagai rintangan. Rintangan-rintangan ini disesuaikan dengan rintangan pada ujian mengemudi sesungguhnya. 

###Cara kerja program

- *User* terlebih dahulu melakukan *login* dengan akun yang sudah terdaftar pada Google Firebase dengan catatan *user* harus lulus simulasi ujian teori terlebih dahulu pada aplikasi Android.
- *User* akan mulai melakukan simulasi ujian dari *stage* pertama, secara keseluruhan terdapat 5 *stage* permainan. Jika *user* berhasil lulus pada suatu *stage* maka *user* dapat melakukan simulasi pada *stage* berikutnya secara bertahap.
- *User* dinyatakan lulus apabila berhasil mencapai seluruh *checkpoint* yang ada pada suatu permainan tanpa terkena satu *obstacle* pada suatu *stage*. Sehingga, bila *user* terkena satu *obstacle* saja maka dinyatakan gagal.

###Konfigurasi tombol yang digunakan pada permainan :

W / :arrow_up: : Maju

A / :arrow_left: :  Belok kiri

S / :arrow_right: :  Belok kanan

D / :arrow_down: : Mundur

Space : Rem tangan

C : *Change view*

Pergerakkan *mouse* digunakan untuk melihat *view* sekeliling *user*.

R : *Restart* permainan (hanya dilakukan ketika *user* gagal dalam suatu *stage*)

Q : Keluar ke menu utama (dilakukan ketika *user* telah menyelesaikan suatu *stage*)