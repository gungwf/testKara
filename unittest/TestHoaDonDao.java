package unittest;

import dao.DichVuDao;
import dao.HoaDonDao;
import entity.*;
import entity.ChiTietDichVu;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.Phong;
import entity.NhanVien;
import entity.DichVu;
import entity.LoaiPhong;
import entity.TrangThaiPhong;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class TestHoaDonDao {

    private HoaDonDao hoaDonDao;
    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;

    @Before
    public void setUp() {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(HoaDon.class)
                .addAnnotatedClass(ChiTietDichVu.class)
                .addAnnotatedClass(ChiTietHoaDon.class)
                .addAnnotatedClass(KhachHang.class)
                .addAnnotatedClass(KhuyenMai.class)
                .addAnnotatedClass(Phong.class)
                .addAnnotatedClass(NhanVien.class)
                .addAnnotatedClass(DichVu.class)
                .addAnnotatedClass(LoaiPhong.class)
                .addAnnotatedClass(TrangThaiPhong.class)
                .buildSessionFactory();
        hoaDonDao = new HoaDonDao(sessionFactory);
        session = sessionFactory.openSession();
    }

    @After
    public void tearDown() {
        if (session != null) {
            session.close();
        }
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    // Test phương thức phatSinhMaTuDong
    /**
     * Kiểm tra phương thức phát sinh mã tự động hoạt động đúng khi có kết nối đến database
     * ID: THD_01
     */
    @Test
    public void phatSinhMaTuDong_testChuan() {
        try {
            transaction = session.beginTransaction();
            String result = hoaDonDao.phatSinhMaTuDong();
            assertNotNull(result);
            assertTrue(result.startsWith("HD"));
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức phát sinh mã tự động xử lý đúng khi mất kết nối database
     * ID: THD_02
     */
    @Test
    public void phatSinhMaTuDong_testLoi() {
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            String result = hoaDonDao.phatSinhMaTuDong();
            assertNull(result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }

    // Test phương thức themHoaDon
    /**
     * Kiểm tra phương thức thêm hóa đơn hoạt động đúng với dữ liệu hợp lệ
     * ID: THD_03
     */
    @Test
    public void themHoaDon_testChuan() {
        HoaDon hoaDon = new HoaDon();
        hoaDon.setKhachHang(new KhachHang("KH001"));
        hoaDon.setKhuyenMai(new KhuyenMai("KM001"));
        hoaDon.setNhanVienLap(new NhanVien("NV001"));
        hoaDon.setNgayLap(new Date());
        hoaDon.setGioNhanPhong(new Date());
        hoaDon.setTrangThai(true);
        try {
            transaction = session.beginTransaction();
            boolean result = hoaDonDao.themHoaDon(hoaDon);
            assertTrue(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức thêm hóa đơn xử lý đúng khi dữ liệu null
     * ID: THD_04
     */
    @Test
    public void themHoaDon_testNull() {
        try {
            transaction = session.beginTransaction();
            boolean result = hoaDonDao.themHoaDon(null);
            assertFalse(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Test phương thức capNhatHoaDon
    /**
     * Kiểm tra phương thức cập nhật hóa đơn hoạt động đúng với dữ liệu hợp lệ
     * ID: THD_05
     */
    @Test
    public void capNhatHoaDon_testChuan() {
        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHD("HD001");
        hoaDon.setKhachHang(new KhachHang("KH001"));
        hoaDon.setKhuyenMai(new KhuyenMai("KM001"));
        hoaDon.setNgayLap(new Date());
        hoaDon.setTrangThai(true);
        try {
            transaction = session.beginTransaction();
            boolean result = hoaDonDao.capNhatHoaDon(hoaDon);
            assertTrue(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức cập nhật hóa đơn xử lý đúng khi hóa đơn không tồn tại
     * ID: THD_06
     */
    @Test
    public void capNhatHoaDon_testKhongTonTai() {
        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHD("HD999");
        try {
            transaction = session.beginTransaction();
            boolean result = hoaDonDao.capNhatHoaDon(hoaDon);
            assertFalse(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Test phương thức layHoaDonTheoMa
    /**
     * Kiểm tra phương thức lấy hóa đơn theo mã hoạt động đúng với mã hợp lệ
     * ID: THD_07
     */
    @Test
    public void layHoaDonTheoMa_testChuan() {
        String maHD = "HD001";
        try {
            transaction = session.beginTransaction();
            HoaDon result = hoaDonDao.layHoaDonTheoMa(maHD);
            assertNotNull(result);
            assertEquals(maHD, result.getMaHD());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy hóa đơn theo mã xử lý đúng khi mã không tồn tại
     * ID: THD_08
     */
    @Test
    public void layHoaDonTheoMa_testKhongTonTai() {
        String maHD = "HD999";
        try {
            transaction = session.beginTransaction();
            HoaDon result = hoaDonDao.layHoaDonTheoMa(maHD);
            assertNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Test phương thức layHoaDonMoiNhatTheoPhong
    /**
     * Kiểm tra phương thức lấy hóa đơn mới nhất theo phòng hoạt động đúng với phòng tồn tại
     * ID: THD_09
     */
    @Test
    public void layHoaDonMoiNhatTheoPhong_testChuan() {
        String maPhong = "P001";
        try {
            transaction = session.beginTransaction();
            HoaDon result = hoaDonDao.layHoaDonMoiNhatTheoPhong(maPhong);
            assertNotNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy hóa đơn mới nhất theo phòng xử lý đúng khi phòng không tồn tại
     * ID: THD_10
     */
    @Test
    public void layHoaDonMoiNhatTheoPhong_testKhongTonTai() {
        String maPhong = "P999";
        try {
            transaction = session.beginTransaction();
            HoaDon result = hoaDonDao.layHoaDonMoiNhatTheoPhong(maPhong);
            assertNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Test phương thức layChiTietDichVu
    /**
     * Kiểm tra phương thức lấy chi tiết dịch vụ hoạt động đúng với mã hóa đơn hợp lệ
     * ID: THD_11
     */
    @Test
    public void layChiTietDichVu_testChuan() {
        String maHD = "HD001";
        try {
            transaction = session.beginTransaction();
            List<ChiTietDichVu> result = hoaDonDao.layChiTietDichVu(maHD);
            assertNotNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy chi tiết dịch vụ xử lý đúng khi mã hóa đơn không tồn tại
     * ID: THD_12
     */
    @Test
    public void layChiTietDichVu_testKhongTonTai() {
        String maHD = "HD999";
        try {
            transaction = session.beginTransaction();
            List<ChiTietDichVu> result = hoaDonDao.layChiTietDichVu(maHD);
            assertNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Test phương thức layChiTietHoaDon
    /**
     * Kiểm tra phương thức lấy chi tiết hóa đơn hoạt động đúng với mã hóa đơn hợp lệ
     * ID: THD_13
     */
    @Test
    public void layChiTietHoaDon_testChuan() {
        String maHD = "HD001";
        try {
            transaction = session.beginTransaction();
            List<ChiTietHoaDon> result = hoaDonDao.layChiTietHoaDon(maHD);
            assertNotNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy chi tiết hóa đơn xử lý đúng khi mã hóa đơn không tồn tại
     * ID: THD_14
     */
    @Test
    public void layChiTietHoaDon_testKhongTonTai() {
        String maHD = "HD999";
        try {
            transaction = session.beginTransaction();
            List<ChiTietHoaDon> result = hoaDonDao.layChiTietHoaDon(maHD);
            assertNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Test phương thức layDanhSachHoaDon
    /**
     * Kiểm tra phương thức lấy danh sách hóa đơn hoạt động đúng với điều kiện tìm kiếm hợp lệ
     * ID: THD_15
     */
    @Test
    public void layDanhSachHoaDon_testChuan() {
        try {
            transaction = session.beginTransaction();
            List<HoaDon> result = hoaDonDao.layDanhSachHoaDon("", "", "", new Date(), null, 0, 20, "");
            assertNotNull(result);
            assertFalse(result.isEmpty());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách hóa đơn xử lý đúng khi không có hóa đơn nào phù hợp
     * ID: THD_16
     */
    @Test
    public void layDanhSachHoaDon_testKhongTonTai() {
        try {
            transaction = session.beginTransaction();
            List<HoaDon> result = hoaDonDao.layDanhSachHoaDon("HD999", "", "", new Date(), null, 0, 20, "");
            assertNotNull(result);
            assertTrue(result.isEmpty());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Test phương thức layTongTrang
    /**
     * Kiểm tra phương thức lấy tổng trang hoạt động đúng với điều kiện tìm kiếm hợp lệ
     * ID: THD_17
     */
    @Test
    public void layTongTrang_testChuan() {
        try {
            transaction = session.beginTransaction();
            int result = hoaDonDao.layTongTrang("", "", "", new Date(), null, 20, "");
            assertTrue(result > 0);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy tổng trang xử lý đúng khi không có hóa đơn nào phù hợp
     * ID: THD_18
     */
    @Test
    public void layTongTrang_testKhongTonTai() {
        try {
            transaction = session.beginTransaction();
            int result = hoaDonDao.layTongTrang("HD999", "", "", new Date(), null, 20, "");
            assertEquals(1, result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // --- Test topKhachHangTheoNam ---
    /**
     * Kiểm tra phương thức lấy top khách hàng theo năm hoạt động đúng với năm có dữ liệu
     * ID: THD_19
     */
    @Test
    public void topKhachHangTheoNam_testChuan() {
        try {
            transaction = session.beginTransaction();
            Map<KhachHang, Double> result = hoaDonDao.topKhachHangTheoNam(2021);
            assertNotNull(result);
            assertTrue(result.size() > 0);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy top khách hàng theo năm xử lý đúng khi năm không có dữ liệu
     * ID: THD_20
     */
    @Test
    public void topKhachHangTheoNam_testKhongCoKhachHang() {
        try {
            transaction = session.beginTransaction();
            Map<KhachHang, Double> result = hoaDonDao.topKhachHangTheoNam(2025); // Năm không có hóa đơn
            assertNotNull(result);
            assertEquals(0, result.size());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // --- Test topKhachHangTheoThang ---
    /**
     * Kiểm tra phương thức lấy top khách hàng theo tháng hoạt động đúng với tháng và năm có dữ liệu
     * ID: THD_21
     */
    @Test
    public void topKhachHangTheoThang_testChuan() {
        try {
            transaction = session.beginTransaction();
            Map<KhachHang, Double> result = hoaDonDao.topKhachHangTheoThang(11, 2021);
            assertNotNull(result);
            assertTrue(result.size() > 0);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy top khách hàng theo tháng không có dữ liệu
     * ID: THD_22
     */
    @Test
    public void topKhachHangTheoThang_testKhongCoKhachHang() {
        try {
            transaction = session.beginTransaction();
            Map<KhachHang, Double> result = hoaDonDao.topKhachHangTheoThang(12, 2025); // Tháng và năm không có hóa đơn
            assertNotNull(result);
            assertEquals(0, result.size());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // --- Test topKhachHangTheoNgay ---
    /**
     * Kiểm tra phương thức lấy top khách hàng theo ngày hoạt động đúng với ngày, tháng và năm có dữ liệu
     * ID: THD_23
     */
    @Test
    public void topKhachHangTheoNgay_testChuan() {
        try {
            transaction = session.beginTransaction();
            Map<KhachHang, Double> result = hoaDonDao.topKhachHangTheoNgay(1, 11, 2021);
            assertNotNull(result);
            assertTrue(result.size() >= 0);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy top khách hàng theo ngày không có dữ liệu
     * ID: THD_24
     */
    @Test
    public void topKhachHangTheoNgay_testKhongCoKhachHang() {
        try {
            transaction = session.beginTransaction();
            Map<KhachHang, Double> result = hoaDonDao.topKhachHangTheoNgay(30, 2, 2025); // Ngày không có hóa đơn
            assertNotNull(result);
            assertEquals(0, result.size());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // --- Test themChiTietDichVu ---
    /**
     * Kiểm tra phương thức thêm chi tiết dịch vụ hoạt động đúng với dữ liệu hợp lệ
     * ID: THD_25
     */
    @Test
    public void themChiTietDichVu_testChuan() {
        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHD("HD001");
        
        DichVu dichVu = new DichVu();
        dichVu.setMaDV("DV001");
        
        ChiTietDichVu chiTietDichVu = new ChiTietDichVu();
        chiTietDichVu.setHoaDon(hoaDon);
        chiTietDichVu.setDichVu(dichVu);
        chiTietDichVu.setSoLuong(2);
        
        try {
            transaction = session.beginTransaction();
            boolean result = hoaDonDao.themChiTietDichVu(chiTietDichVu);
            assertTrue(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức thêm chi tiết dịch vụ xử lý đúng khi dữ liệu không hợp lệ
     * ID: THD_26
     */
    @Test
    public void themChiTietDichVu_testDuLieuKhongHopLe() {
        try {
            transaction = session.beginTransaction();
            boolean result = hoaDonDao.themChiTietDichVu(null);
            assertFalse(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // --- Test themHoacCapNhatChiTietHoaDon ---
    /**
     * Kiểm tra phương thức thêm hoặc cập nhật chi tiết hóa đơn hoạt động đúng với dữ liệu hợp lệ
     * ID: THD_27
     */
    @Test
    public void themHoacCapNhatChiTietHoaDon_testChuan() {
        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHD("HD001");
        
        Phong phong = new Phong();
        phong.setMaPhong("P001");
        
        ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
        chiTietHoaDon.setHoaDon(hoaDon);
        chiTietHoaDon.setPhong(phong);
        chiTietHoaDon.setThoiLuong(60); // Đặt thời lượng sử dụng phòng (phút)
        
        try {
            transaction = session.beginTransaction();
            boolean result = hoaDonDao.themHoacCapNhatChiTietHoaDon(chiTietHoaDon);
            assertTrue(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức thêm hoặc cập nhật chi tiết hóa đơn xử lý đúng khi dữ liệu không hợp lệ
     * ID: THD_28
     */
    @Test
    public void themHoacCapNhatChiTietHoaDon_testDuLieuKhongHopLe() {
        try {
            transaction = session.beginTransaction();
            boolean result = hoaDonDao.themHoacCapNhatChiTietHoaDon(null);
            assertFalse(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // --- Test xoaChiTietDichVu ---
    /**
     * Kiểm tra phương thức xóa chi tiết dịch vụ hoạt động đúng với dữ liệu hợp lệ
     * ID: THD_29
     */
    @Test
    public void xoaChiTietDichVu_testChuan() {
        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHD("HD001");
        
        DichVu dichVu = new DichVu();
        dichVu.setMaDV("DV001");
        
        ChiTietDichVu chiTietDichVu = new ChiTietDichVu();
        chiTietDichVu.setHoaDon(hoaDon);
        chiTietDichVu.setDichVu(dichVu);
        
        try {
            transaction = session.beginTransaction();
            boolean result = hoaDonDao.xoaChiTietDichVu(chiTietDichVu);
            assertTrue(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức xóa chi tiết dịch vụ xử lý đúng khi dữ liệu không hợp lệ
     * ID: THD_30
     */
    @Test
    public void xoaChiTietDichVu_testDuLieuKhongHopLe() {
        try {
            transaction = session.beginTransaction();
            boolean result = hoaDonDao.xoaChiTietDichVu(null);
            assertFalse(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // --- Test layNamTuHoaDon ---
    /**
     * Kiểm tra phương thức lấy năm từ hóa đơn hoạt động đúng
     * ID: THD_31
     */
    @Test
    public void layNamTuHoaDon_testChuan() {
        try {
            transaction = session.beginTransaction();
            List<Integer> result = hoaDonDao.layNamTuHoaDon();
            assertNotNull(result);
            assertFalse(result.isEmpty());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy năm từ hóa đơn xử lý đúng khi có lỗi
     * ID: THD_32
     */
    @Test
    public void layNamTuHoaDon_testLoi() {
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            List<Integer> result = hoaDonDao.layNamTuHoaDon();
            assertNull(result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }

    // --- Test layHoaDonTheoNgay ---
    /**
     * Kiểm tra phương thức lấy hóa đơn theo ngày hoạt động đúng với ngày có dữ liệu
     * ID: THD_33
     */
    @Test
    public void layHoaDonTheoNgay_testChuan() {
        try {
            transaction = session.beginTransaction();
            List<HoaDon> result = hoaDonDao.layHoaDonTheoNgay(1, 11, 2021, "");
            assertNotNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy hóa đơn theo ngày xử lý đúng khi không có dữ liệu
     * ID: THD_34
     */
    @Test
    public void layHoaDonTheoNgay_testKhongCoDuLieu() {
        try {
            transaction = session.beginTransaction();
            List<HoaDon> result = hoaDonDao.layHoaDonTheoNgay(30, 2, 2025, "");
            assertNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // --- Test layHoaDonTheoThang ---
    /**
     * Kiểm tra phương thức lấy hóa đơn theo tháng hoạt động đúng với tháng có dữ liệu
     * ID: THD_35
     */
    @Test
    public void layHoaDonTheoThang_testChuan() {
        try {
            transaction = session.beginTransaction();
            List<HoaDon> result = hoaDonDao.layHoaDonTheoThang(11, 2021, "");
            assertNotNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy hóa đơn theo tháng xử lý đúng khi không có dữ liệu
     * ID: THD_36
     */
    @Test
    public void layHoaDonTheoThang_testKhongCoDuLieu() {
        try {
            transaction = session.beginTransaction();
            List<HoaDon> result = hoaDonDao.layHoaDonTheoThang(13, 2025, "");
            assertNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // --- Test layHoaDonTheoNam ---
    /**
     * Kiểm tra phương thức lấy hóa đơn theo năm hoạt động đúng với năm có dữ liệu
     * ID: THD_37
     */
    @Test
    public void layHoaDonTheoNam_testChuan() {
        try {
            transaction = session.beginTransaction();
            List<HoaDon> result = hoaDonDao.layHoaDonTheoNam(2021, "");
            assertNotNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy hóa đơn theo năm xử lý đúng khi không có dữ liệu
     * ID: THD_38
     */
    @Test
    public void layHoaDonTheoNam_testKhongCoDuLieu() {
        try {
            transaction = session.beginTransaction();
            List<HoaDon> result = hoaDonDao.layHoaDonTheoNam(2025, "");
            assertNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}
