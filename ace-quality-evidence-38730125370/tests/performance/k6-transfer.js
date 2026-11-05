import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter, Rate, Trend } from 'k6/metrics';

const transferErrors = new Rate('transfer_errors');
const transferLatency = new Trend('transfer_latency', true);
const accepted = new Counter('accepted_transfers');
const baseUrl = __ENV.BASE_URL || 'http://localhost:8080';

export const options = {
  scenarios: {
    steady_transfers: {
      executor: 'constant-arrival-rate',
      rate: 5,
      timeUnit: '1s',
      duration: '2m',
      preAllocatedVUs: 10,
      maxVUs: 30,
    },
  },
  thresholds: {
    http_req_failed: ['rate<0.01'],
    transfer_latency: ['p(95)<500'],
    transfer_errors: ['rate<0.01'],
  },
};

export default function () {
  const runId = __ENV.RUN_ID || 'local';
  const key = `k6-${runId}-${__VU}-${__ITER}`;
  const traceId = `${__VU.toString(16).padStart(16, '0')}${__ITER.toString(16).padStart(16, '0')}`;
  const spanId = `${(__VU * 1000000 + __ITER).toString(16).padStart(16, '0')}`;
  const payload = JSON.stringify({
    fromAccount: 'SYN-CHK-001',
    beneficiaryId: 'SYN-BEN-001',
    amount: 1,
    currency: 'USD',
    idempotencyKey: key,
  });
  const response = http.post(`${baseUrl}/api/v1/transfers`, payload, {
    headers: {
      'Content-Type': 'application/json',
      'Idempotency-Key': key,
      'traceparent': `00-${traceId}-${spanId}-01`,
    },
    timeout: '5s',
  });

  const passed = check(response, {
    'transfer accepted or expected synthetic balance exhausted': (r) => r.status === 202 || r.status === 409,
    'accepted response includes transfer reference': (r) =>
      r.status !== 202 || Boolean(r.json('transferId')),
  });
  transferErrors.add(!passed);
  transferLatency.add(response.timings.duration);
  if (response.status === 202) {
    accepted.add(1);
  }
  sleep(0.1);
}
