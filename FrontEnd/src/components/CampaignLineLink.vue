<template>
  <div class="line-chat-bubble">
    <div class="chat-content">
      <p class="chat-text">
        Hey！想收到最新優惠資訊嗎？<br />
        快來加入我們的好友吧<span class="typing-dots"></span>
      </p>
    </div>
    <div class="line-button">
      <a href="https://line.me/R/ti/p/@750umglq" target="_blank">
        <img
          height="36"
          src="https://scdn.line-apps.com/n/line_add_friends/btn/zh-Hant.png"
          alt="加入好友"
        />
      </a>
    </div>
  </div>
</template>

<style scoped>
.line-chat-bubble {
  position: fixed;
  right: 30px;
  bottom: 30px;
  z-index: 1000;
  font-family: 'Microsoft JhengHei', sans-serif;
}

.chat-content {
  background: #00c300;
  padding: 15px 20px;
  border-radius: 15px;
  max-width: 250px;
  margin-bottom: 10px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  animation: bounce 2s infinite;
  transition: all 0.3s ease;
}

.chat-content:hover {
  transform: scale(1.05);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
}

.chat-text {
  color: white;
  font-size: 15px;
  line-height: 1.4;
  margin: 0;
  text-align: center;
}

.line-button {
  background: white;
  padding: 10px;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  float: right;
}

.line-button:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
}

@keyframes bounce {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

.typing-dots {
  display: inline-block;
}

.typing-dots::after {
  content: '...';
  animation: typing 1.5s infinite;
  font-weight: bold;
}

@keyframes typing {
  0%,
  100% {
    content: '';
  }
  25% {
    content: '.';
  }
  50% {
    content: '..';
  }
  75% {
    content: '...';
  }
}
</style>
