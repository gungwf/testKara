-- 🛑 XÓA DATABASE NẾU CÓ VÀ TẠO LẠI MỚI
DROP DATABASE IF EXISTS KaraokeMeMe;
CREATE DATABASE KaraokeMeMe DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE KaraokeMeMe;

-- 1️⃣ Tạo bảng DiaChi
CREATE TABLE DiaChi (
    maDC VARCHAR(10) NOT NULL,
    phuongXa TEXT NULL,
    quanHuyen TEXT NULL,
    tinhTP TEXT NULL,
    PRIMARY KEY (maDC)
);

-- 2️⃣ Tạo bảng LoaiNhanVien
CREATE TABLE LoaiNhanVien (
    maLNV VARCHAR(10) NOT NULL,
    tenLoaiNhanVien TEXT NULL,
    PRIMARY KEY (maLNV)
);
select * from LoaiNhanVien;

-- 3️⃣ Tạo bảng TrangThaiPhong
CREATE TABLE TrangThaiPhong (
    maTTP VARCHAR(10) NOT NULL,
    tenTrangThaiPhong TEXT NULL,
    PRIMARY KEY (maTTP)
);

-- 4️⃣ Tạo bảng LoaiPhong
CREATE TABLE LoaiPhong (
    maLP VARCHAR(10) NOT NULL,
    giaTien DECIMAL(10,2) NOT NULL,
    tenLoaiPhong TEXT NULL,
    PRIMARY KEY (maLP)
);

-- 5️⃣ Tạo bảng KhuyenMai
CREATE TABLE KhuyenMai (
    maKM VARCHAR(7) NOT NULL,
    chietKhau DECIMAL(10,2) NOT NULL,
    daSuDung INT NOT NULL,
    maGiamGia VARCHAR(10) NULL,
    moTa TEXT NULL,
    ngayBatDau DATETIME NULL,
    ngayHetHan DATETIME NULL,
    tongSoLuong INT NOT NULL,
    trangThai TINYINT(1) NOT NULL,
    PRIMARY KEY (maKM)
);

-- 6️⃣ Tạo bảng MaGiamGia
CREATE TABLE MaGiamGia (
    maMGG VARCHAR(10) NOT NULL,
    chietKhau DECIMAL(10,2) NOT NULL,
    daSuDung INT NOT NULL,
    ngayBatDau DATETIME NULL,
    ngayHetHan DATETIME NULL,
    tenMaGiamGia TEXT NULL,
    tongSoLuong INT NOT NULL,
    PRIMARY KEY (maMGG)
);

-- 7️⃣ Tạo bảng KhachHang
CREATE TABLE KhachHang (
    maKH VARCHAR(10) NOT NULL,
    gioiTinh TINYINT(1) NOT NULL,
    hoTen TEXT NULL,
    ngaySinh DATETIME NULL,
    soDienThoai VARCHAR(11) NULL,
    maDC VARCHAR(10) NULL,
    PRIMARY KEY (maKH),
    FOREIGN KEY (maDC) REFERENCES DiaChi(maDC) ON DELETE SET NULL
);

-- 8️⃣ Tạo bảng NhanVien
CREATE TABLE NhanVien (
    maNV VARCHAR(10) NOT NULL,
    gioiTinh TINYINT(1) NOT NULL,
    hoTen VARCHAR(255) NOT NULL,
    ngaySinh DATETIME NULL,
    password VARCHAR(255) NOT NULL,
    soCMND VARCHAR(11) NOT NULL,
    soDienThoai VARCHAR(11) NOT NULL,
    trangThaiLamViec TINYINT(1) NOT NULL,
    maDC VARCHAR(10) NULL,
    maLNV VARCHAR(10) NULL,
    PRIMARY KEY (maNV),
    FOREIGN KEY (maDC) REFERENCES DiaChi(maDC) ON DELETE SET NULL,
    FOREIGN KEY (maLNV) REFERENCES LoaiNhanVien(maLNV) ON DELETE SET NULL
);

-- 9️⃣ Tạo bảng Phong
CREATE TABLE Phong (
    maPhong VARCHAR(10) NOT NULL,
    soNguoi INT NOT NULL,
    tenPhong TEXT NULL,
    tinhTrangPhong TINYINT(1) NOT NULL,
    maLP VARCHAR(10) NULL,
    maTTP VARCHAR(10) NULL,
    PRIMARY KEY (maPhong),
    FOREIGN KEY (maLP) REFERENCES LoaiPhong(maLP) ON DELETE SET NULL,
    FOREIGN KEY (maTTP) REFERENCES TrangThaiPhong(maTTP) ON DELETE SET NULL
);

-- 🔟 Tạo bảng HoaDon
CREATE TABLE HoaDon (
    maHD VARCHAR(10) NOT NULL,
    chietKhau DECIMAL(10,2) NOT NULL,
    gioKetThuc DATETIME NULL,
    gioNhanPhong DATETIME NULL,
    ngayLap DATETIME NULL,
    thue DECIMAL(10,2) NOT NULL,
    tienKhachTra DECIMAL(10,2) NOT NULL,
    maKH VARCHAR(10) NULL,
    maMGG VARCHAR(10) NULL,
    maKM VARCHAR(7) NULL,
    maNV VARCHAR(10) NULL,
    PRIMARY KEY (maHD),
    FOREIGN KEY (maKH) REFERENCES KhachHang(maKH) ON DELETE SET NULL,
    FOREIGN KEY (maMGG) REFERENCES MaGiamGia(maMGG) ON DELETE SET NULL,
    FOREIGN KEY (maKM) REFERENCES KhuyenMai(maKM) ON DELETE SET NULL,
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV) ON DELETE SET NULL
);

-- 1️⃣1️⃣ Tạo bảng ChiTietHoaDon
CREATE TABLE ChiTietHoaDon (
    maHD VARCHAR(10) NOT NULL,
    maPhong VARCHAR(10) NOT NULL,
    thoiLuong INT NOT NULL,
    PRIMARY KEY (maHD, maPhong),
    FOREIGN KEY (maHD) REFERENCES HoaDon(maHD) ON DELETE CASCADE,
    FOREIGN KEY (maPhong) REFERENCES Phong(maPhong) ON DELETE CASCADE
);

-- 1️⃣2️⃣ Tạo bảng DichVu
CREATE TABLE DichVu (
    maDV VARCHAR(10) NOT NULL,
    donGia DECIMAL(10,2) NULL,
    donViTinh VARCHAR(50) NULL,
    soLuong INT NOT NULL,
    tenDichVu TEXT NULL,
    trangThaiDichVu TINYINT(1) NOT NULL,
    PRIMARY KEY (maDV)
);

-- 1️⃣3️⃣ Tạo bảng ChiTietDichVu
CREATE TABLE ChiTietDichVu (
    maDV VARCHAR(10) NOT NULL,
    maHD VARCHAR(10) NOT NULL,
    soLuong INT NOT NULL,
    PRIMARY KEY (maDV, maHD),
    FOREIGN KEY (maHD) REFERENCES HoaDon(maHD) ON DELETE CASCADE,
    FOREIGN KEY (maDV) REFERENCES DichVu(maDV) ON DELETE CASCADE
);

-- 1️⃣ Dữ liệu cho bảng DiaChi
INSERT INTO DiaChi (maDC, phuongXa, quanHuyen, tinhTP) VALUES
('DC001', 'Phường 1', 'Quận 1', 'TP Hồ Chí Minh'),
('DC002', 'Phường 2', 'Quận 2', 'Hà Nội'),
('DC003', 'Phường 3', 'Quận 3', 'Đà Nẵng');

-- 2️⃣ Dữ liệu cho bảng LoaiNhanVien
INSERT INTO LoaiNhanVien (maLNV, tenLoaiNhanVien) VALUES
('LNV001', 'Quản lý'),
('LNV002', 'Nhân viên thu ngân'),
('LNV003', 'Nhân viên phục vụ');

-- 3️⃣ Dữ liệu cho bảng TrangThaiPhong
INSERT INTO TrangThaiPhong (maTTP, tenTrangThaiPhong) VALUES
('TTP001', 'Trống'),
('TTP002', 'Đang sử dụng'),
('TTP003', 'Bảo trì');

-- 4️⃣ Dữ liệu cho bảng LoaiPhong
INSERT INTO LoaiPhong (maLP, giaTien, tenLoaiPhong) VALUES
('LP001', 200000, 'Phòng thường'),
('LP002', 500000, 'Phòng VIP'),
('LP003', 1000000, 'Phòng VIP Pro');

-- 5️⃣ Dữ liệu cho bảng KhuyenMai
INSERT INTO KhuyenMai (maKM, chietKhau, daSuDung, maGiamGia, moTa, ngayBatDau, ngayHetHan, tongSoLuong, trangThai) VALUES
('KM001', 10, 5, 'MGG001', 'Giảm giá 10% nhân dịp lễ', '2025-01-01', '2025-12-31', 100, 1),
('KM002', 20, 3, 'MGG002', 'Giảm giá 20% cho khách VIP', '2025-02-01', '2025-12-31', 50, 1);

select * from khuyenmai;

-- 6️⃣ Dữ liệu cho bảng MaGiamGia
INSERT INTO MaGiamGia (maMGG, chietKhau, daSuDung, ngayBatDau, ngayHetHan, tenMaGiamGia, tongSoLuong) VALUES
('MGG001', 10, 5, '2025-01-01', '2025-12-31', 'Mã giảm giá lễ hội', 100),
('MGG002', 20, 3, '2025-02-01', '2025-12-31', 'Mã giảm giá khách VIP', 50);

-- 7️⃣ Dữ liệu cho bảng KhachHang
INSERT INTO KhachHang (maKH, gioiTinh, hoTen, ngaySinh, soDienThoai, maDC) VALUES
('KH001', 1, 'Nguyễn Văn A', '1990-05-20', '0123456789', 'DC001'),
('KH002', 0, 'Trần Thị B', '1995-07-15', '0987654321', 'DC002');

-- 8️⃣ Dữ liệu cho bảng NhanVien
INSERT INTO NhanVien (maNV, gioiTinh, hoTen, ngaySinh, password, soCMND, soDienThoai, trangThaiLamViec, maDC, maLNV) VALUES
('NV001', 1, 'Lê Minh Quản', '1985-03-10', '123456', '12345678901', '0123456789', 1, 'DC001', 'LNV001'),
('NV002', 1, 'Phạm Thị Thu Ngân', '1992-06-25', 'abcdef', '12345678902', '0987654321', 1, 'DC002', 'LNV002');

-- 9️⃣ Dữ liệu cho bảng Phong
INSERT INTO Phong (maPhong, soNguoi, tenPhong, tinhTrangPhong, maLP, maTTP) VALUES
('P001', 4, 'Phòng 101', 1, 'LP001', 'TTP001'),
('P002', 8, 'Phòng 102', 1, 'LP002', 'TTP002'),
('P003', 12, 'Phòng 103', 0, 'LP003', 'TTP003');

-- 🔟 Dữ liệu cho bảng HoaDon
INSERT INTO HoaDon (maHD, chietKhau, gioKetThuc, gioNhanPhong, ngayLap, thue, tienKhachTra, maKH, maMGG, maKM, maNV) VALUES
('HD001', 10, '2025-03-16 22:00:00', '2025-03-16 20:00:00', '2025-03-16', 5, 500000, 'KH001', 'MGG001', 'KM001', 'NV001');

-- 1️⃣1️⃣ Dữ liệu cho bảng ChiTietHoaDon
INSERT INTO ChiTietHoaDon (maHD, maPhong, thoiLuong) VALUES
('HD001', 'P001', 2);

-- 1️⃣2️⃣ Dữ liệu cho bảng DichVu
INSERT INTO DichVu (maDV, donGia, donViTinh, soLuong, tenDichVu, trangThaiDichVu) VALUES
('DV001', 50000, 'Lon', 50, 'Bia Tiger', 1),
('DV002', 100000, 'Dĩa', 30, 'Trái cây tổng hợp', 1);

-- 1️⃣3️⃣ Dữ liệu cho bảng ChiTietDichVu
INSERT INTO ChiTietDichVu (maDV, maHD, soLuong) VALUES
('DV001', 'HD001', 5),
('DV002', 'HD001', 2);


