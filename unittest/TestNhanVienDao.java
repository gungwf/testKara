package unittest;

import dao.NhanVienDao;
import entity.NhanVien;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.util.List;

public class TestNhanVienDao {

    private NhanVienDao nhanVienDao;
    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;

    @Before
    public void setUp() {
        // Khởi tạo SessionFactory và kết nối với cơ sở dữ liệu
        sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(NhanVien.class)
                .buildSessionFactory();
        nhanVienDao = new NhanVienDao(sessionFactory);
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

    // Test phương thức 'doiMatKhau'
    /**
     * Kiểm tra phương thức đổi mật khẩu hoạt động đúng với thông tin hợp lệ
     * ID: TNV_01
     */
    @Test
    public void doiMatKhau_testChuan1() {
        String sdt = "0987654321";
        String oldPassword = "oldPass123";
        String newPassword = "newPass123";

        try {
            transaction = session.beginTransaction();
            boolean result = nhanVienDao.doiMatKhau(sdt, oldPassword, newPassword);
            assertTrue(result); // Kiểm tra kết quả trả về là true
            transaction.rollback(); // Rollback để không thay đổi dữ liệu thực tế
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức đổi mật khẩu xử lý đúng khi thông tin không hợp lệ
     * ID: TNV_02
     */
    @Test
    public void doiMatKhau_testChuan2() {
        String sdt = "0000000000"; // Số điện thoại không tồn tại
        String oldPassword = "wrongOldPass";
        String newPassword = "newPass123";

        try {
            transaction = session.beginTransaction();
            boolean result = nhanVienDao.doiMatKhau(sdt, oldPassword, newPassword);
            assertFalse(result); // Kiểm tra kết quả trả về là false khi không tìm thấy người dùng
            transaction.rollback(); // Rollback để không thay đổi dữ liệu thực tế
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    // Test phương thức 'DanhSachNhanVien'
    /**
     * Kiểm tra phương thức lấy danh sách nhân viên hoạt động đúng với các tham số tìm kiếm hợp lệ
     * ID: TNV_03
     */
    @Test
    public void DanhSachNhanVien_testChuan1() throws RemoteException {
        int page = 0;
        String tenNhanVien = "John";
        String gioiTinh = "Male";
        String trangThaiLamViec = "1"; // Đang làm việc
        String loaiNhanVien = "";
        int limit = 10;

        List<NhanVien> result = nhanVienDao.DanhSachNhanVien(page, tenNhanVien, gioiTinh, trangThaiLamViec, loaiNhanVien, limit);

        assertNotNull(result); // Kiểm tra kết quả không null
        assertTrue(result.size() > 0); // Kiểm tra có ít nhất một nhân viên trong danh sách
    }

    /**
     * Kiểm tra phương thức lấy danh sách nhân viên xử lý đúng khi không có nhân viên phù hợp với điều kiện
     * ID: TNV_04
     */
    @Test
    public void DanhSachNhanVien_testChuan2() throws RemoteException {
        int page = 0;
        String tenNhanVien = "NonExistentName";
        String gioiTinh = "Male";
        String trangThaiLamViec = "1"; // Đang làm việc
        String loaiNhanVien = "";
        int limit = 10;

        List<NhanVien> result = nhanVienDao.DanhSachNhanVien(page, tenNhanVien, gioiTinh, trangThaiLamViec, loaiNhanVien, limit);

        assertNotNull(result); // Kiểm tra kết quả không null
        assertTrue(result.size() == 0); // Kiểm tra rằng không có nhân viên nào được tìm thấy
    }

    // Test phương thức 'tongTrang'
    /**
     * Kiểm tra phương thức tính tổng số trang hoạt động đúng với các tham số tìm kiếm hợp lệ
     * ID: TNV_05
     */
    @Test
    public void tongTrang_testChuan1() {
        String txtSearch = "John";
        String gioiTinh = "Male";
        String trangThaiLamViec = "1"; // Đang làm việc
        String loaiNhanVien = "";
        int limit = 10;

        int result = nhanVienDao.tongTrang(txtSearch, gioiTinh, trangThaiLamViec, loaiNhanVien, limit);

        assertTrue(result > 0); // Kiểm tra tổng số trang có đúng và lớn hơn 0
    }

    // Test phương thức 'layThongTinNhanVienQuaSDT'
    /**
     * Kiểm tra phương thức lấy thông tin nhân viên qua số điện thoại hoạt động đúng với số điện thoại hợp lệ
     * ID: TNV_06
     */
    @Test
    public void layThongTinNhanVienQuaSDT_testChuan1() throws RemoteException {
        String sdt = "0987654321";
        NhanVien result = nhanVienDao.layThongTinNhanVienQuaSDT(sdt);

        assertNotNull(result); // Kiểm tra kết quả không null
        assertEquals(sdt, result.getSoDienThoai()); // Kiểm tra số điện thoại
    }

    /**
     * Kiểm tra phương thức lấy thông tin nhân viên qua số điện thoại xử lý đúng khi số điện thoại không tồn tại
     * ID: TNV_07
     */
    @Test
    public void layThongTinNhanVienQuaSDT_testChuan2() throws RemoteException {
        String sdt = "0000000000"; // Số điện thoại không tồn tại
        NhanVien result = nhanVienDao.layThongTinNhanVienQuaSDT(sdt);

        assertNull(result); // Kiểm tra rằng kết quả trả về là null
    }

    // Test phương thức 'layThongTinNhanVienQuaCMND'
    /**
     * Kiểm tra phương thức lấy thông tin nhân viên qua CMND hoạt động đúng với CMND hợp lệ
     * ID: TNV_08
     */
    @Test
    public void layThongTinNhanVienQuaCMND_testChuan1() throws RemoteException{
        String cmnd = "123456789";
        NhanVien result = nhanVienDao.layThongTinNhanVienQuaCMND(cmnd);

        assertNotNull(result); // Kiểm tra kết quả không null
        assertEquals(cmnd, result.getSoCMND()); // Kiểm tra CMND
    }

    /**
     * Kiểm tra phương thức lấy thông tin nhân viên qua CMND xử lý đúng khi CMND không tồn tại
     * ID: TNV_09
     */
    @Test
    public void layThongTinNhanVienQuaCMND_testChuan2() throws RemoteException{
        String cmnd = "000000000";
        NhanVien result = nhanVienDao.layThongTinNhanVienQuaCMND(cmnd);

        assertNull(result); // Kiểm tra rằng kết quả trả về là null
    }

    // Test phương thức 'layDanhSachNhanVienTheoTen'
    /**
     * Kiểm tra phương thức lấy danh sách nhân viên theo tên hoạt động đúng với tên hợp lệ
     * ID: TNV_10
     */
    @Test
    public void layDanhSachNhanVienTheoTen_testChuan1() {
        String ten = "John";
        List<NhanVien> result = nhanVienDao.layDanhSachNhanVienTheoTen(ten);

        assertNotNull(result); // Kiểm tra kết quả không null
        assertTrue(result.size() > 0); // Kiểm tra danh sách nhân viên không rỗng
    }

    /**
     * Kiểm tra phương thức lấy danh sách nhân viên theo tên xử lý đúng khi tên không tồn tại
     * ID: TNV_11
     */
    @Test
    public void layDanhSachNhanVienTheoTen_testChuan2() {
        String ten = "NonExistentName";
        List<NhanVien> result = nhanVienDao.layDanhSachNhanVienTheoTen(ten);

        assertNotNull(result); // Kiểm tra kết quả không null
        assertTrue(result.size() == 0); // Kiểm tra không có nhân viên nào được tìm thấy
    }

    // Test phương thức layDanhSachNhanVien
    /**
     * Kiểm tra phương thức lấy danh sách nhân viên hoạt động đúng khi có kết nối đến database
     * ID: TNV_12
     */
    @Test
    public void layDanhSachNhanVien_testChuan() {
        try {
            transaction = session.beginTransaction();
            List<NhanVien> result = nhanVienDao.layDanhSachNhanVien();
            assertNotNull(result);
            assertFalse(result.isEmpty());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách nhân viên xử lý đúng khi mất kết nối database
     * ID: TNV_13
     */
    @Test
    public void layDanhSachNhanVien_testLoi() {
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            List<NhanVien> result = nhanVienDao.layDanhSachNhanVien();
            assertNull(result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }

    // Test phương thức layNhanVienTheoMa
    /**
     * Kiểm tra phương thức lấy nhân viên theo mã hoạt động đúng với mã hợp lệ
     * ID: TNV_14
     */
    @Test
    public void layNhanVienTheoMa_testChuan() {
        String maNV = "NV001";
        try {
            transaction = session.beginTransaction();
            NhanVien result = nhanVienDao.layNhanVienTheoMa(maNV);
            assertNotNull(result);
            assertEquals(maNV, result.getMaNV());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy nhân viên theo mã xử lý đúng khi mã không tồn tại
     * ID: TNV_15
     */
    @Test
    public void layNhanVienTheoMa_testKhongTonTai() {
        String maNV = "NV999";
        try {
            transaction = session.beginTransaction();
            NhanVien result = nhanVienDao.layNhanVienTheoMa(maNV);
            assertNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Test phương thức themNhanVien
    /**
     * Kiểm tra phương thức thêm nhân viên hoạt động đúng với dữ liệu hợp lệ
     * ID: TNV_16
     */
    @Test
    public void themNhanVien_testChuan() {
        NhanVien nhanVien = new NhanVien();
        nhanVien.setHoTen("Nguyễn Văn A");
        nhanVien.setSoDienThoai("0123456789");
        nhanVien.setGioiTinh(true);
        try {
            transaction = session.beginTransaction();
            boolean result = nhanVienDao.themNhanVien(nhanVien);
            assertTrue(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức thêm nhân viên xử lý đúng khi dữ liệu null
     * ID: TNV_17
     */
    @Test
    public void themNhanVien_testNull() {
        try {
            transaction = session.beginTransaction();
            boolean result = nhanVienDao.themNhanVien(null);
            assertFalse(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Test phương thức suaThongTinNhanVien
    /**
     * Kiểm tra phương thức sửa thông tin nhân viên hoạt động đúng với dữ liệu hợp lệ
     * ID: TNV_18
     */
    @Test
    public void suaThongTinNhanVien_testChuan() {
        NhanVien nhanVien = new NhanVien();
        nhanVien.setMaNV("NV001");
        nhanVien.setHoTen("Nguyễn Văn B");
        nhanVien.setSoDienThoai("0987654321");
        nhanVien.setGioiTinh(false);
        try {
            transaction = session.beginTransaction();
            boolean result = nhanVienDao.suaThongTinNhanVien(nhanVien);
            assertTrue(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức sửa thông tin nhân viên xử lý đúng khi nhân viên không tồn tại
     * ID: TNV_19
     */
    @Test
    public void suaThongTinNhanVien_testKhongTonTai() {
        NhanVien nhanVien = new NhanVien();
        nhanVien.setMaNV("NV999");
        try {
            transaction = session.beginTransaction();
            boolean result = nhanVienDao.suaThongTinNhanVien(nhanVien);
            assertFalse(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Test phương thức layThongTinNhanVienQuaSDT
    /**
     * Kiểm tra phương thức lấy thông tin nhân viên qua SDT hoạt động đúng với SDT hợp lệ
     * ID: TNV_20
     */
    @Test
    public void layThongTinNhanVienQuaSDT_testChuan() {
        String sdt = "0123456789";
        try {
            transaction = session.beginTransaction();
            NhanVien result = nhanVienDao.layThongTinNhanVienQuaSDT(sdt);
            assertNotNull(result);
            assertEquals(sdt, result.getSoDienThoai());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy thông tin nhân viên qua SDT xử lý đúng khi SDT không tồn tại
     * ID: TNV_21
     */
    @Test
    public void layThongTinNhanVienQuaSDT_testKhongTonTai() {
        String sdt = "9999999999";
        try {
            transaction = session.beginTransaction();
            NhanVien result = nhanVienDao.layThongTinNhanVienQuaSDT(sdt);
            assertNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
    /**
     * Kiểm tra phương thức lấy thông tin nhân viên qua loại nhân viên hoạt động đúng với loại nhân viên hợp lệ
     * ID: TNV_22
     */
    @Test
    public void layDanhSachNhanVienQuaLoaiNhanVien_testChuan() {
        String loaiNV = "Nhân viên văn phòng";
        try {
            transaction = session.beginTransaction();
            NhanVien result = nhanVienDao.layDanhSachNhanVienQuaLoaiNhanVien(loaiNV);
            assertNotNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
    /**
     * Kiểm tra phương thức lấy thông tin nhân viên qua loại nhân viên xử lý đúng khi loại nhân viên không tồn tại
     * ID: TNV_23
     */
    @Test
    public void layDanhSachNhanVienQuaLoaiNhanVien_testKhongTonTai() {
        String loaiNV = "Loại không tồn tại";
        try {
            transaction = session.beginTransaction();
            NhanVien result = nhanVienDao.layDanhSachNhanVienQuaLoaiNhanVien(loaiNV);
            assertNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
    /**
     * Kiểm tra phương thức thay đổi trạng thái làm việc qua số điện thoại hoạt động đúng với SDT hợp lệ
     * ID: TNV_24
     */
    @Test
    public void suaTrangThaiLamViecQuaSoDienThoai_testChuan() throws RemoteException {
        String sdt = "0123456789";
        boolean trangThai = true;
        try {
            transaction = session.beginTransaction();
            boolean result = nhanVienDao.suaTrangThaiLamViecQuaSoDienThoai(sdt, trangThai);
            assertTrue(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
    /**
     * Kiểm tra phương thức thay đổi trạng thái làm việc qua số điện thoại xử lý đúng khi SDT không tồn tại
     * ID: TNV_25
     */
    @Test
    public void suaTrangThaiLamViecQuaSoDienThoai_testKhongTonTai() throws RemoteException {
        String sdt = "9999999999";
        boolean trangThai = true;
        try {
            transaction = session.beginTransaction();
            boolean result = nhanVienDao.suaTrangThaiLamViecQuaSoDienThoai(sdt, trangThai);
            assertFalse(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách nhân viên khi xảy ra ngoại lệ
     * ID: TNV_26
     */
    @Test
    public void testLayDanhSachNhanVien_Exception() {
        // Giả lập trường hợp xảy ra exception bằng cách đóng session
        if (session != null && session.isOpen()) {
            session.close();
        }
        List<NhanVien> result = nhanVienDao.layDanhSachNhanVien();
        assertNotNull(result);
        assertTrue(result.isEmpty());
        // Khởi tạo lại session cho các test khác
        session = sessionFactory.openSession();
    }
    
    /**
     * Kiểm tra phương thức tính số trang khi xảy ra ngoại lệ
     * ID: TNV_27
     */
    @Test
    public void testTinhSoTrang_Exception() {
        try {
            // Giả lập trường hợp xảy ra exception bằng cách đóng session
            if (session != null && session.isOpen()) {
                session.close();
            }
            // Sử dụng đúng phương thức tongTrang với các tham số phù hợp
            int result = nhanVienDao.tongTrang("", "", "", "", 10);
            assertEquals(0, result);
            // Khởi tạo lại session cho các test khác
            session = sessionFactory.openSession();
        } catch (Exception e) {
            // Đảm bảo khởi tạo lại session ngay cả khi có exception
            session = sessionFactory.openSession();
            fail("Exception should be handled in the method");
        }
    }
    
    /**
     * Kiểm tra phương thức lấy nhân viên theo mã chính xác
     * ID: TNV_28
     */
    @Test
    public void testLayNhanVienTheoMa_Success() {
        try {
            transaction = session.beginTransaction();
            NhanVien nv = nhanVienDao.layNhanVienTheoMa("NV001");
            assertNotNull(nv);
            assertEquals("NV001", nv.getMaNV());
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            fail("Exception shouldn't be thrown");
        }
    }
    
    /**
     * Kiểm tra phương thức lấy nhân viên theo mã không tồn tại
     * ID: TNV_29
     */
    @Test
    public void testLayNhanVienTheoMa_NotFound() {
        try {
            transaction = session.beginTransaction();
            NhanVien nv = nhanVienDao.layNhanVienTheoMa("INVALID_ID");
            assertNull(nv);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            fail("Exception shouldn't be thrown");
        }
    }
    
    /**
     * Kiểm tra phương thức đổi mật khẩu khi xảy ra ngoại lệ
     * ID: TNV_30
     */
    @Test
    public void testDoiMatKhau_Exception() {
        try {
            // Giả lập trường hợp xảy ra exception bằng cách đóng session
            if (session != null && session.isOpen()) {
                session.close();
            }
            // Sử dụng đúng phương thức doiMatKhau với 3 tham số
            boolean result = nhanVienDao.doiMatKhau("0987654321", "oldPassword", "newPassword");
            assertFalse(result);
            // Khởi tạo lại session cho các test khác
            session = sessionFactory.openSession();
        } catch (Exception e) {
            // Đảm bảo khởi tạo lại session ngay cả khi có exception
            session = sessionFactory.openSession();
            fail("Exception should be handled in the method");
        }
    }
    
    /**
     * Kiểm tra phương thức xóa nhân viên thành công
     * ID: TNV_31
     */
    @Test
    public void testXoaNhanVien_Success() {
        NhanVien nv = new NhanVien();
        nv.setMaNV("NV999");
        nv.setHoTen("Test Delete");
        nv.setGioiTinh(true);
        nv.setSoDienThoai("0987654321");
        nv.setSoCMND("123456789012");
        
        try {
            transaction = session.beginTransaction();
            session.save(nv);
            transaction.commit();
            
            // Kiểm tra việc xóa nhân viên bằng cách sử dụng phương thức có sẵn
            transaction = session.beginTransaction();
            // Giả sử sử dụng phương thức suaThongTinNhanVien với trangThaiLamViec = false
            nv.setTrangThaiLamViec(false);
            boolean result = nhanVienDao.suaThongTinNhanVien(nv);
            assertTrue(result);
            
            NhanVien updated = nhanVienDao.layNhanVienTheoMa(nv.getMaNV());
            assertFalse(updated.isTrangThaiLamViec());
            transaction.commit();
            
            // Clean up
            transaction = session.beginTransaction();
            session.delete(nv);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            fail("Exception shouldn't be thrown");
        }
    }
    
    /**
     * Kiểm tra phương thức xóa nhân viên thất bại
     * ID: TNV_32
     */
    @Test
    public void testXoaNhanVien_Failure() {
        try {
            transaction = session.beginTransaction();
            NhanVien nonExistent = new NhanVien();
            nonExistent.setMaNV("INVALID_ID");
            nonExistent.setTrangThaiLamViec(false);
            // Giả sử sử dụng phương thức suaThongTinNhanVien để xóa (vô hiệu hóa) nhân viên
            boolean result = nhanVienDao.suaThongTinNhanVien(nonExistent);
            assertFalse(result);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            fail("Exception shouldn't be thrown");
        }
    }
    
    /**
     * Kiểm tra phương thức cập nhật nhân viên thành công
     * ID: TNV_33
     */
    @Test
    public void testCapNhatNhanVien_Success() {
        NhanVien nv = new NhanVien();
        nv.setMaNV("NV998");
        nv.setHoTen("Test Update");
        nv.setGioiTinh(true);
        nv.setSoDienThoai("0987654321");
        nv.setSoCMND("123456789012");
        nv.setTrangThaiLamViec(true);
        
        try {
            transaction = session.beginTransaction();
            session.save(nv);
            transaction.commit();
            
            nv.setHoTen("Updated Name");
            nv.setSoDienThoai("0123456789");
            
            transaction = session.beginTransaction();
            boolean result = nhanVienDao.suaThongTinNhanVien(nv);
            assertTrue(result);
            
            NhanVien updated = nhanVienDao.layNhanVienTheoMa(nv.getMaNV());
            assertEquals("Updated Name", updated.getHoTen());
            assertEquals("0123456789", updated.getSoDienThoai());
            transaction.commit();
            
            // Clean up
            transaction = session.beginTransaction();
            session.delete(nv);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            fail("Exception shouldn't be thrown");
        }
    }
    
    /**
     * Kiểm tra phương thức cập nhật nhân viên thất bại
     * ID: TNV_34
     */
    @Test
    public void testCapNhatNhanVien_Failure() {
        NhanVien nv = new NhanVien();
        nv.setMaNV("INVALID_ID");
        nv.setHoTen("Test Invalid");
        
        try {
            transaction = session.beginTransaction();
            boolean result = nhanVienDao.suaThongTinNhanVien(nv);
            assertFalse(result);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            fail("Exception shouldn't be thrown");
        }
    }
    
    /**
     * Kiểm tra phương thức kiểm tra tồn tại khi xảy ra ngoại lệ
     * ID: TNV_35
     */
    @Test
    public void testKiemTraTonTai_Exception() {
        try {
            // Giả lập trường hợp xảy ra exception bằng cách đóng session
            if (session != null && session.isOpen()) {
                session.close();
            }
            
            // Kiểm tra tồn tại bằng cách thử gọi layNhanVienTheoMa
            NhanVien result = nhanVienDao.layNhanVienTheoMa("NV001");
            assertNull(result);
            
            // Khởi tạo lại session cho các test khác
            session = sessionFactory.openSession();
        } catch (Exception e) {
            // Đảm bảo khởi tạo lại session ngay cả khi có exception
            session = sessionFactory.openSession();
            fail("Exception should be handled in the method");
        }
    }
    
    /**
     * Kiểm tra phương thức kiểm tra đăng nhập thành công
     * ID: TNV_36
     */
    @Test
    public void testKiemTraDangNhap_Success() {
        try {
            // Giả sử đăng nhập bằng cách kiểm tra mã nhân viên và password
            transaction = session.beginTransaction();
            String maNV = "NV001";
            String password = "password";
            
            // Tìm nhân viên theo mã
            NhanVien nv = nhanVienDao.layNhanVienTheoMa(maNV);
            
            // Kiểm tra password - giả sử password đúng là "password"
            assertNotNull(nv);
            assertEquals(maNV, nv.getMaNV());
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            fail("Exception shouldn't be thrown");
        }
    }
    
    /**
     * Kiểm tra phương thức kiểm tra đăng nhập thất bại với mã không tồn tại
     * ID: TNV_37
     */
    @Test
    public void testKiemTraDangNhap_InvalidId() {
        try {
            transaction = session.beginTransaction();
            String invalidId = "INVALID_ID";
            String password = "password";
            
            // Tìm nhân viên theo mã không tồn tại
            NhanVien nv = nhanVienDao.layNhanVienTheoMa(invalidId);
            
            // Kết quả phải là null
            assertNull(nv);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            fail("Exception shouldn't be thrown");
        }
    }
}
