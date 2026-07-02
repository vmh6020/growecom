# Bức Tranh Hoàn Hảo: Kiến Trúc Spring Security (Interactive Boxes)

Dưới đây là sơ đồ luồng hệ thống được thiết kế dưới dạng **Các Khối Hộp Tương Tác (Interactive Boxes)**. 
Bạn hãy **BẤM VÀO TỪNG HỘP** để mở rộng thông tin bên trong. Bấm lần nữa để đóng lại. Thiết kế này giúp bạn đi theo đúng mạch tư duy mà không bị rối mắt.

---

<details style="border: 2px solid #0288d1; border-radius: 8px; margin-bottom: 15px; padding: 5px; box-shadow: 2px 2px 5px rgba(0,0,0,0.1);">
    <summary style="font-size: 1.3em; font-weight: bold; cursor: pointer; padding: 12px; background-color: #e1f5fe; border-radius: 4px; color: #01579b;">
        📦 1. TẦNG GIAO TIẾP (BearerTokenAuthenticationFilter)
    </summary>
    <div style="padding: 15px; background-color: #fafafa; border-top: 1px solid #eee; margin-top: 5px; line-height: 1.6;">
        <p><b>📌 Vai trò:</b> Đây là lính gác vòng ngoài cùng, chặn mọi Request đi vào hệ thống.</p>
        <p><b>📥 Đầu vào:</b> HTTP Request mang theo Header <code>Authorization: Bearer ey...</code></p>
        <p><b>⚙️ Hành động:</b> Cắt bỏ chữ "Bearer ", lấy ra chuỗi JWT nguyên thủy.</p>
        <p><b>📤 Đầu ra:</b> Đóng gói chuỗi đó thành một tấm thẻ chưa xác thực: <code>BearerTokenAuthenticationToken</code>.</p>
        
        <details style="border: 1px dashed #0288d1; border-radius: 5px; margin-top: 15px; padding: 5px;">
            <summary style="font-weight: bold; cursor: pointer; padding: 10px; background-color: #f1f8e9; color: #33691e;">
                ↪️ Chuyển giao tiếp theo...
            </summary>
            <div style="padding: 10px;">
                <p>Ném tấm thẻ chưa xác thực này cho <b>AuthenticationManager</b> để nhờ kiểm tra giùm.</p>
            </div>
        </details>
    </div>
</details>

<details style="border: 2px solid #f57c00; border-radius: 8px; margin-bottom: 15px; padding: 5px; box-shadow: 2px 2px 5px rgba(0,0,0,0.1);">
    <summary style="font-size: 1.3em; font-weight: bold; cursor: pointer; padding: 12px; background-color: #fff3e0; border-radius: 4px; color: #e65100;">
        📦 2. TẦNG QUẢN ĐỐC (AuthenticationManager / ProviderManager)
    </summary>
    <div style="padding: 15px; background-color: #fafafa; border-top: 1px solid #eee; margin-top: 5px; line-height: 1.6;">
        <p><b>📌 Vai trò:</b> Là người điều phối, quản lý danh sách các xưởng xác thực (Provider).</p>
        <p><b>📥 Đầu vào:</b> Tấm thẻ <code>BearerTokenAuthenticationToken</code> từ lính gác truyền tới.</p>
        <p><b>⚙️ Hành động:</b> Quét qua danh sách các Provider xem ai có khả năng xử lý loại thẻ JWT này.</p>
        <p><b>📤 Đầu ra:</b> Uỷ quyền (Delegate) nhiệm vụ cho <code>JwtAuthenticationProvider</code>.</p>
    </div>
</details>

<details style="border: 2px solid #e64a19; border-radius: 8px; margin-bottom: 15px; padding: 5px; box-shadow: 2px 2px 5px rgba(0,0,0,0.1);">
    <summary style="font-size: 1.3em; font-weight: bold; cursor: pointer; padding: 12px; background-color: #fbe9e7; border-radius: 4px; color: #bf360c;">
        📦 3. TẦNG XỬ LÝ (JwtAuthenticationProvider)
    </summary>
    <div style="padding: 15px; background-color: #fafafa; border-top: 1px solid #eee; margin-top: 5px; line-height: 1.6;">
        <p><b>📌 Vai trò:</b> Xưởng chịu trách nhiệm kiểm tra tính hợp lệ của JWT.</p>
        <p><b>📥 Đầu vào:</b> Tấm thẻ mang chuỗi JWT cần kiểm tra.</p>
        
        <details style="border: 1px solid #ffccbc; border-radius: 5px; margin-top: 10px; margin-bottom: 10px; padding: 5px;">
            <summary style="font-weight: bold; cursor: pointer; padding: 10px; background-color: #fff; color: #d84315;">
                🔍 Mở rộng: Quá trình Giải mã JWT (NimbusJwtDecoder)
            </summary>
            <div style="padding: 10px; border-left: 3px solid #ff7043; margin-left: 10px;">
                <p><b>⚙️ Hành động:</b> Provider vứt chuỗi JWT cho <code>NimbusJwtDecoder</code>.</p>
                <p><b>⚙️ Kiểm tra:</b> Dùng SecretKeySpec (HMAC SHA-256) đối chiếu chữ ký. Kiểm tra hạn sử dụng (exp).</p>
                <p><b>📤 Kết quả:</b> Nếu đúng, Parse chuỗi JSON thành một Object <code>org...oauth2.jwt.Jwt</code> (chứa trọn vẹn Payload).</p>
            </div>
        </details>

        <details style="border: 1px solid #ffccbc; border-radius: 5px; margin-bottom: 10px; padding: 5px;">
            <summary style="font-weight: bold; cursor: pointer; padding: 10px; background-color: #fff; color: #d84315;">
                🔄 Mở rộng: Quá trình Convert Role & Đúc Thẻ Xanh
            </summary>
            <div style="padding: 10px; border-left: 3px solid #ff7043; margin-left: 10px;">
                <p><b>⚙️ Hành động:</b> Provider vứt Object <code>Jwt</code> vừa lấy được vào cho <code>JwtAuthenticationConverter</code>.</p>
                <p><b>⚙️ Kiểm tra:</b> Móc trường "role" (ví dụ: ADMIN) ra, biến nó thành danh sách quyền (GrantedAuthorities).</p>
                <p><b>📤 Kết quả:</b> Đúc ra chiếc thẻ cuối cùng: <code>JwtAuthenticationToken</code>.</p>
            </div>
        </details>
        
        <p><b>📤 Đầu ra cuối cùng:</b> Trả chiếc thẻ <code>JwtAuthenticationToken</code> (lúc này cờ <code>authenticated = true</code>) ngược lại cho Quản đốc.</p>
    </div>
</details>

<details style="border: 2px solid #388e3c; border-radius: 8px; margin-bottom: 15px; padding: 5px; box-shadow: 2px 2px 5px rgba(0,0,0,0.1);">
    <summary style="font-size: 1.3em; font-weight: bold; cursor: pointer; padding: 12px; background-color: #e8f5e9; border-radius: 4px; color: #1b5e20;">
        📦 4. KÉT SẮT LƯU TRỮ (SecurityContextHolder)
    </summary>
    <div style="padding: 15px; background-color: #fafafa; border-top: 1px solid #eee; margin-top: 5px; line-height: 1.6;">
        <p><b>📌 Vai trò:</b> Lưu trữ thẻ thông hành trong RAM để hệ thống sử dụng.</p>
        <p><b>📥 Đầu vào:</b> Chiếc thẻ xanh <code>JwtAuthenticationToken</code> được Quản đốc truyền ngược về Lính gác (Filter).</p>
        <p><b>⚙️ Hành động:</b> Lính gác gọi lệnh <code>SecurityContextHolder.getContext().setAuthentication(token)</code> để cất chiếc thẻ này vào ThreadLocal của RAM.</p>
        <p><b>📤 Đầu ra:</b> Xong nhiệm vụ bảo vệ! Mở cổng cho Request chạy thẳng vào Controller/Service của bạn.</p>
    </div>
</details>

<details style="border: 2px solid #7b1fa2; border-radius: 8px; margin-bottom: 15px; padding: 5px; box-shadow: 2px 2px 5px rgba(0,0,0,0.1);">
    <summary style="font-size: 1.3em; font-weight: bold; cursor: pointer; padding: 12px; background-color: #f3e5f5; border-radius: 4px; color: #4a148c;">
        📦 5. TẦNG ỨNG DỤNG (Cách bạn dùng ở Controller/Service)
    </summary>
    <div style="padding: 15px; background-color: #fafafa; border-top: 1px solid #eee; margin-top: 5px; line-height: 1.6;">
        <p><b>📌 Tại Service (Ví dụ: OrderService):</b></p>
        <p>Hệ thống đã cất thẻ, việc của bạn chỉ là móc thẻ ra dùng:</p>
        <pre style="background-color: #f5f5f5; padding: 10px; border-radius: 5px; border-left: 4px solid #7b1fa2;">
// 1. Mở két sắt lấy thẻ ra
Authentication auth = SecurityContextHolder.getContext().getAuthentication();

// 2. Mở ruột thẻ ra (Principal). Ta ép kiểu luôn thành Jwt.
Jwt jwt = (Jwt) auth.getPrincipal();

// 3. Lấy thông tin email và chọc DB.
String email = jwt.getSubject();
        </pre>
    </div>
</details>
