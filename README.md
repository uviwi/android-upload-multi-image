# Upload Multiple Image Using Fast Android Networking & PHP

![N|Solid](https://uviwi.id/wp-content/uploads/2021/01/logo-name-e1610693837737.png)
## This Project Using Libraries :
* [Fast Android Networking](https://github.com/amitshekhariitbhu/Fast-Android-Networking)
* [Pix Image Picker](https://github.com/akshay2211/PixImagePicker)

## This below for php script
```
<?php
# Tampung data file yang di kirim
$file = $_FILES['file'];

# Lakukan perhitungan jumlah file yang di kirim
$jumlah_file = count($_FILES['file']['name']);

$dir = "uploads/"; // variabel ini untuk nama lokasi file

# Cek directory
if (!file_exists($dir)) :  // jika directory tidak ada
    mkdir($dir, 0777); // buat directory dengan nama yg sudah di definisikan pada variabel $dir
    chmod($dir, 0777); // ubah permission directory (Opsional)
endif;

# Lakukan perulangan untuk mendapatkan file per item
for ($i=0; $i < $jumlah_file ; $i++) :

    # Tampung nama File per item kedalam variabel
    $name = $file['name'][$i]; // variabel ini berisikan nama file

    $tmp_name = $file['tmp_name'][$i]; // variabel ini berisikan nama file sementara sebelum di kirim

    $upload = move_uploaded_file($tmp_name, $dir . $name); // variabel ini untuk melakukan pemindahan file dari file yang di simpan sementara ($tmp_name) ke lokasi yang di tentukan pada variabel $dir dengan nama yang di tentukan pada variabel $name (nama file asli).
endfor;

if($upload) :
    echo json_encode(['status' => true,'message' => 'berhasil']);
else :
    echo json_encode(['status' => false,'message' => 'gagal']);
endif;

```

## For tutorial, click this link below
https://uviwi.id/android-tutorial-cara-upload-multiple-image-menggunakan-php/
