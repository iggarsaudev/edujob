export interface AttendanceResponse {
  id: string;
  studentDni: string;
  timestamp: string;
  type: string; // 'ENTRY' o 'EXIT'
  message?: string;
}
