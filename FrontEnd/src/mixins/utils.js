export const utils = () => {
  const statusFilters = {
    IN_PROGRESS: {
      text: '進行中',
      class: 'text-success',
    },
    EXPIRED: {
      text: '已結束',
      class: 'text-danger',
    },
    NOT_STARTED: {
      text: '未開始',
      class: 'text-secondary',
    },
  }

  const getStatusDisplay = (status) => statusFilters[status] || statusFilters['NOT_STARTED']

  const formatDate = (dateString) => {
    const date = new Date(dateString)
    return date.toLocaleString('zh-TW', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      hour12: false,
    })
  }

  const currency = (num) => {
    const n = parseInt(num, 10)
    return `${n.toFixed(0).replace(/./g, (c, i, a) => (i && c !== '.' && (a.length - i) % 3 === 0 ? `, ${c}`.replace(/\s/g, '') : c))}`
  }

  return {
    statusFilters,
    formatDate,
    getStatusDisplay,
    currency,
  }
}
