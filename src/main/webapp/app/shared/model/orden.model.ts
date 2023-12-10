import { Modo } from 'app/shared/model/enumerations/modo.model';

export interface IOrden {
  id?: number;
  cliente?: number;
  accionId?: number;
  accion?: string | null;
  operacion?: string;
  cantidad?: number;
  precio?: number;
  fechaOperacion?: string | null;
  modo?: keyof typeof Modo;
  procesada?: boolean | null;
}

export const defaultValue: Readonly<IOrden> = {
  procesada: false,
};
