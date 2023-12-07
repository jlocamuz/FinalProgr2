export interface IOrden {
  id?: number;
  cliente?: number;
  accionId?: number;
  accion?: string | null;
  operacion?: string;
  cantidad?: number;
  precio?: number;
  fechaOperacion?: string | null;
  modo?: string;
}

export const defaultValue: Readonly<IOrden> = {};
