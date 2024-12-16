// countdownUtils.js

export function startCountdown(orders, updateOrder) {
  orders.forEach((order, index) => {
    if (order.status === '等待中' && order.countdown > 0) {
      const interval = setInterval(() => {
        if (order.countdown <= 0) {
          clearInterval(interval);
        } else {
          orders[index].countdown -= 1;
          updateOrder(index, orders[index]);
        }
      }, 1000);
    }
  });
}

export function formatCountdown(seconds) {
  const minutes = Math.floor(seconds / 60);
  const remainingSeconds = seconds % 60;
  return `${String(minutes).padStart(2, '0')}:${String(remainingSeconds).padStart(2, '0')}`;
}
