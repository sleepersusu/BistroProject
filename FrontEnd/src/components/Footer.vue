<template>
  <div class="bg-dark">
    <div class="container">
      <footer class="py-5 text-light">
        <div class="row">
          <div class="col-md-2 col-4">
            <h5>探索</h5>
            <ul class="nav flex-column">
              <li class="nav-item mb-2"><a href="#" class="nav-link p-0 text-light">首頁</a></li>
              <li class="nav-item mb-2">
                <a href="#" class="nav-link p-0 text-light">關於酌夜</a>
              </li>
              <li class="nav-item mb-2">
                <a href="#" class="nav-link p-0 text-light">最新消息</a>
              </li>
            </ul>
          </div>

          <div class="col-md-2 col-4">
            <h5>美食饗宴</h5>
            <ul class="nav flex-column">
              <li class="nav-item mb-2">
                <a href="#" class="nav-link p-0 text-light">菜單一覽</a>
              </li>
              <li class="nav-item mb-2">
                <a href="#" class="nav-link p-0 text-light">主廚推薦</a>
              </li>
              <li class="nav-item mb-2">
                <a href="#" class="nav-link p-0 text-light">季節限定</a>
              </li>
            </ul>
          </div>

          <div class="col-md-2 col-4">
            <h5>聯絡資訊</h5>
            <ul class="nav flex-column">
              <li class="nav-item mb-2">
                <span class="nav-link p-0 text-light">營業時間：17:00-23:00</span>
              </li>
              <li class="nav-item mb-2">
                <span class="nav-link p-0 text-light">電話：02-2345-6789</span>
              </li>
              <li class="nav-item mb-2">
                <span class="nav-link p-0 text-light">地址：台北市中山區南京東路三段123號</span>
              </li>
            </ul>
          </div>

          <div class="col-lg-4 offset-1 ms-0 mt-3 mt-lg-0 ms-lg-5">
            <form>
              <h5>訂閱最新消息</h5>
              <p>訂閱電子報，隨時掌握最新優惠及活動資訊</p>
              <div class="d-flex w-100 gap-2">
                <label for="newsletter1" class="visually-hidden">Email address</label>
                <input
                  id="newsletter1"
                  type="text"
                  class="form-control"
                  placeholder="Email address"
                />
                <button class="btn btn-outline-light" type="button">Subscribe</button>
              </div>
            </form>
          </div>
        </div>

        <div class="d-flex justify-content-between py-4 my-4 border-top border-primary">
          <p>&copy; 2024 酌夜語 Nightly Sips. All rights reserved.</p>
          <ul class="list-unstyled d-flex">
            <li class="ms-4">
              <a class="link-light" target="_blank" href="https://www.facebook.com/"
                ><i class="bi bi-facebook fs-4"></i
              ></a>
            </li>
            <li class="ms-4">
              <a class="link-light" target="_blank" href="https://www.instagram.com/"
                ><i class="bi bi-instagram fs-4"></i
              ></a>
            </li>
            <li class="ms-4">
              <a class="link-light" target="_blank" href="https://www.threads.net/"
                ><i class="bi bi-threads fs-4"></i
              ></a>
            </li>
          </ul>
        </div>
      </footer>
    </div>
  </div>
</template>

<script>
export default {}
</script>
