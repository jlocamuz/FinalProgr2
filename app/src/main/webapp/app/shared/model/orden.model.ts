export interface IOrden {
  id?: number;
  cliente?: number | null;
  accionId?: number | null;
  accion?: string | null;
  operacion?: string | null;
  cantidad?: number | null;
  precio?: number | null;
  fechaOperacion?: string | null;
  modo?: string | null;
  operacionExitosa?: boolean | null;
  operacionObservaciones?: string | null;
}

export const defaultValue: Readonly<IOrden> = {
  operacionExitosa: false,
};
