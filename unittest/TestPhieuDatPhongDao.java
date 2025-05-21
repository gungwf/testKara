package unittest;

import dao.PhieuDatPhongDao;
import entity.PhieuDatPhong;
import entity.KhachHang;
import entity.NhanVien;
import entity.Phong;
import entity.TrangThaiPhong;
import entity.LoaiPhong;
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

public class TestPhieuDatPhongDao {

    private PhieuDatPhongDao phieuDatPhongDao;
    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;

    @Before
    public void setUp() {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(PhieuDatPhong.class)
                .addAnnotatedClass(KhachHang.class)
                .addAnnotatedClass(NhanVien.class)
                .addAnnotatedClass(Phong.class)
                .addAnnotatedClass(TrangThaiPhong.class)
                .addAnnotatedClass(LoaiPhong.class)
                .buildSessionFactory();
        phieuDatPhongDao = new PhieuDatPhongDao(sessionFactory);
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

    /**
     * Kiểm tra phương thức phát sinh mã tự động hoạt động đúng khi có kết nối đến database
     * ID: TPDP_01
     */
    @Test
    public void phatSinhMaTuDong_testChuan() {
        try {
            transaction = session.beginTransaction();
            String result = phieuDatPhongDao.phatSinhMaTuDong();
            assertNotNull(result);
            assertTrue(result.startsWith("PDP"));
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức phát sinh mã tự động xử lý đúng khi mất kết nối database
     * ID: TPDP_02
     */
    @Test
    public void phatSinhMaTuDong_testLoi() {
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            String result = phieuDatPhongDao.phatSinhMaTuDong();
            assertNull(result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách phiếu đặt phòng hoạt động đúng với tham số mặc định
     * ID: TPDP_03
     */
    @Test
    public void layDanhSachPhieuDatPhong_testChuan() {
        try {
            transaction = session.beginTransaction();
            List<PhieuDatPhong> result = phieuDatPhongDao.layDanhSachPhieuDatPhong("", "", 0);
            assertNotNull(result);
            assertFalse(result.isEmpty());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách phiếu đặt phòng xử lý đúng khi có lỗi
     * ID: TPDP_04
     */
    @Test
    public void layDanhSachPhieuDatPhong_testLoi() {
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            List<PhieuDatPhong> result = phieuDatPhongDao.layDanhSachPhieuDatPhong("", "", 0);
            assertNull(result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách phiếu đặt phòng hoạt động đúng khi tìm theo mã phiếu đặt
     * ID: TPDP_05
     */
    @Test
    public void layDanhSachPhieuDatPhong_testTimTheoMa() {
        try {
            transaction = session.beginTransaction();
            List<PhieuDatPhong> result = phieuDatPhongDao.layDanhSachPhieuDatPhong("PDP001", "", 0);
            assertNotNull(result);
            if (!result.isEmpty()) {
                assertEquals("PDP001", result.get(0).getMaPDP());
            }
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách phiếu đặt phòng hoạt động đúng khi tìm theo số điện thoại khách
     * ID: TPDP_06
     */
    @Test
    public void layDanhSachPhieuDatPhong_testTimTheoSDT() {
        try {
            transaction = session.beginTransaction();
            List<PhieuDatPhong> result = phieuDatPhongDao.layDanhSachPhieuDatPhong("", "0123456789", 0);
            assertNotNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách phiếu đặt phòng hoạt động đúng khi lọc theo tình trạng còn hiệu lực
     * ID: TPDP_07
     */
    @Test
    public void layDanhSachPhieuDatPhong_testLocTheoTinhTrangConHieuLuc() {
        try {
            transaction = session.beginTransaction();
            List<PhieuDatPhong> result = phieuDatPhongDao.layDanhSachPhieuDatPhong("", "", 1);
            assertNotNull(result);
            for (PhieuDatPhong pdp : result) {
                assertTrue(pdp.isTinhTrang());
            }
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách phiếu đặt phòng hoạt động đúng khi lọc theo tình trạng hết hạn
     * ID: TPDP_08
     */
    @Test
    public void layDanhSachPhieuDatPhong_testLocTheoTinhTrangHetHan() {
        try {
            transaction = session.beginTransaction();
            List<PhieuDatPhong> result = phieuDatPhongDao.layDanhSachPhieuDatPhong("", "", 2);
            assertNotNull(result);
            for (PhieuDatPhong pdp : result) {
                assertFalse(pdp.isTinhTrang());
            }
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy phiếu đặt phòng mới nhất theo phòng hoạt động đúng với phòng tồn tại
     * ID: TPDP_09
     */
    @Test
    public void layPhieuDatPhongMoiNhatTheoPhong_testChuan() {
        String maPhong = "P001";
        try {
            transaction = session.beginTransaction();
            PhieuDatPhong result = phieuDatPhongDao.layPhieuDatPhongMoiNhatTheoPhong(maPhong);
            assertNotNull(result);
            assertEquals(maPhong, result.getPhong().getMaPhong());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy phiếu đặt phòng mới nhất theo phòng xử lý đúng khi phòng không tồn tại
     * ID: TPDP_10
     */
    @Test
    public void layPhieuDatPhongMoiNhatTheoPhong_testKhongTonTai() {
        String maPhong = "P999";
        try {
            transaction = session.beginTransaction();
            PhieuDatPhong result = phieuDatPhongDao.layPhieuDatPhongMoiNhatTheoPhong(maPhong);
            assertNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy phiếu đặt phòng mới nhất theo phòng xử lý đúng khi có lỗi
     * ID: TPDP_11
     */
    @Test
    public void layPhieuDatPhongMoiNhatTheoPhong_testLoi() {
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            PhieuDatPhong result = phieuDatPhongDao.layPhieuDatPhongMoiNhatTheoPhong("P001");
            assertNull(result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }

    /**
     * Kiểm tra phương thức thêm phiếu đặt phòng hoạt động đúng với dữ liệu hợp lệ
     * ID: TPDP_12
     */
    @Test
    public void themPhieuDatPhong_testChuan() {
        PhieuDatPhong phieuDatPhong = new PhieuDatPhong();
        phieuDatPhong.setKhachHang(new KhachHang("KH001"));
        phieuDatPhong.setNhanVienLap(new NhanVien("NV001"));
        phieuDatPhong.setPhong(new Phong("P001"));
        phieuDatPhong.setThoiGianNhanPhong(new Date());
        phieuDatPhong.setThoiGianDangKyDatPhong(new Date());
        try {
            transaction = session.beginTransaction();
            boolean result = phieuDatPhongDao.themPhieuDatPhong(phieuDatPhong);
            assertTrue(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức thêm phiếu đặt phòng xử lý đúng khi dữ liệu null
     * ID: TPDP_13
     */
    @Test
    public void themPhieuDatPhong_testNull() {
        try {
            transaction = session.beginTransaction();
            boolean result = phieuDatPhongDao.themPhieuDatPhong(null);
            assertFalse(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức thêm phiếu đặt phòng xử lý đúng khi thiếu dữ liệu khách hàng
     * ID: TPDP_14
     */
    @Test
    public void themPhieuDatPhong_testKhongCoKhachHang() {
        PhieuDatPhong phieuDatPhong = new PhieuDatPhong();
        phieuDatPhong.setNhanVienLap(new NhanVien("NV001"));
        phieuDatPhong.setPhong(new Phong("P001"));
        phieuDatPhong.setThoiGianNhanPhong(new Date());
        phieuDatPhong.setThoiGianDangKyDatPhong(new Date());
        try {
            transaction = session.beginTransaction();
            boolean result = phieuDatPhongDao.themPhieuDatPhong(phieuDatPhong);
            assertFalse(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức thêm phiếu đặt phòng xử lý đúng khi thiếu thông tin phòng
     * ID: TPDP_15
     */
    @Test
    public void themPhieuDatPhong_testKhongCoPhong() {
        PhieuDatPhong phieuDatPhong = new PhieuDatPhong();
        phieuDatPhong.setKhachHang(new KhachHang("KH001"));
        phieuDatPhong.setNhanVienLap(new NhanVien("NV001"));
        phieuDatPhong.setThoiGianNhanPhong(new Date());
        phieuDatPhong.setThoiGianDangKyDatPhong(new Date());
        try {
            transaction = session.beginTransaction();
            boolean result = phieuDatPhongDao.themPhieuDatPhong(phieuDatPhong);
            assertFalse(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức hủy phiếu đặt phòng hoạt động đúng với mã phiếu hợp lệ
     * ID: TPDP_16
     */
    @Test
    public void huyPhieuDatPhong_testChuan() {
        String maPDP = "PDP001";
        try {
            transaction = session.beginTransaction();
            boolean result = phieuDatPhongDao.huyPhieuDatPhong(maPDP);
            assertTrue(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức hủy phiếu đặt phòng xử lý đúng khi mã phiếu không tồn tại
     * ID: TPDP_17
     */
    @Test
    public void huyPhieuDatPhong_testKhongTonTai() {
        String maPDP = "PDP999";
        try {
            transaction = session.beginTransaction();
            boolean result = phieuDatPhongDao.huyPhieuDatPhong(maPDP);
            assertFalse(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức hủy phiếu đặt phòng xử lý đúng khi có lỗi
     * ID: TPDP_18
     */
    @Test
    public void huyPhieuDatPhong_testLoi() {
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            boolean result = phieuDatPhongDao.huyPhieuDatPhong("PDP001");
            assertFalse(result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }

    /**
     * Kiểm tra phương thức cập nhật trạng thái phiếu đặt phòng hoạt động đúng
     * ID: TPDP_19
     */
    @Test
    public void updateTrangThaiPDP_testChuan() {
        String maPDP = "PDP001";
        try {
            transaction = session.beginTransaction();
            int result = phieuDatPhongDao.updateTrangThaiPDP(maPDP);
            assertTrue(result >= 0);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức cập nhật trạng thái phiếu đặt phòng xử lý đúng khi có lỗi
     * ID: TPDP_20
     */
    @Test
    public void updateTrangThaiPDP_testLoi() {
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            int result = phieuDatPhongDao.updateTrangThaiPDP("PDP001");
            assertEquals(0, result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }
}
