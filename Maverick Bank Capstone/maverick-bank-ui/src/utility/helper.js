// The token is saved in the local storage at the login (same as the other screens)
export const authHeader = () => {
    return { headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') } }
}

// One message for every kind of error: the backend message, the validation messages, or a simple text
export const getErrorMessage = (err) => {
    const status = err?.response?.status
    const data = err?.response?.data

    if (status === 401) return 'Your session has expired. Please login again.'
    if (status === 403) return 'You are not allowed to do this.'
    // the default error body of the server has only technical values (timestamp, status, path)
    if (status >= 500) return 'Something went wrong on the server. Please check the details and try again.'
    if (data === undefined) return 'The server is not reachable. Please try again later.'
    if (data?.message) return data.message
    if (typeof data === 'object' && data !== null && Object.keys(data).length > 0) {
        // validation errors come as { field: message }
        return Object.values(data).join(', ')
    }
    return 'Something went wrong...'
}

// yyyy-mm-dd of a date (the format of the date input)
export const toInputDate = (date) => {
    const pad = (n) => String(n).padStart(2, '0')
    return date.getFullYear() + '-' + pad(date.getMonth() + 1) + '-' + pad(date.getDate())
}

export const formatMoney = (amount) => {
    return Number(amount).toLocaleString('en-IN', { style: 'currency', currency: 'INR' })
}

export const formatDateTime = (value) => {
    return value ? new Date(value).toLocaleString('en-IN') : '-'
}

// Bootstrap colour of a status badge
export const statusBadge = (status) => {
    switch (status) {
        case 'ACTIVE':
        case 'APPROVED':
        case 'SUCCESS':
        case 'DISBURSED':
            return 'text-bg-success'
        case 'PENDING':
            return 'text-bg-warning'
        case 'REJECTED':
        case 'INACTIVE':
        case 'FAILED':
            return 'text-bg-danger'
        default:
            return 'text-bg-secondary'
    }
}

export const ACCOUNT_TYPES = ['SAVINGS', 'CHECKING', 'BUSINESS']

// Filter - statuses of an account (used when the accounts are listed)
export const ACCOUNT_STATUSES = ['PENDING', 'ACTIVE', 'CLOSED']

export const CITIES = ['MUMBAI', 'DELHI', 'BENGALURU', 'HYDERABAD', 'CHENNAI', 'KOLKATA', 'PUNE', 'AHMEDABAD', 'JAIPUR', 'SURAT',
    'LUCKNOW', 'KANPUR', 'NAGPUR', 'INDORE', 'BHOPAL', 'VISAKHAPATNAM', 'PATNA', 'VADODARA', 'AGRA', 'VARANASI', 'KOCHI',
    'CHANDIGARH', 'COIMBATORE', 'MYSORE', 'AMRITSAR', 'GOA', 'THIRUVANANTHAPURAM', 'BHUBANESWAR', 'RANCHI', 'DEHRADUN',
    'GUWAHATI', 'UDAIPUR', 'NASHIK', 'AURANGABAD', 'RAIPUR', 'VIJAYAWADA', 'MADURAI', 'TIRUCHIRAPPALLI', 'MANGALORE',
    'JODHPUR', 'HARIDWAR', 'RISHIKESH', 'SHIMLA', 'SRINAGAR', 'PONDICHERRY']
