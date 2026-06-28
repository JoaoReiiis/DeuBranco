import type { SelectHTMLAttributes } from 'react';
import styles from './Select.module.scss';

interface SelectOption {
  value: string;
  label: string;
}

interface SelectProps extends SelectHTMLAttributes<HTMLSelectElement> {
  label?: string;
  error?: string;
  options: SelectOption[];
  placeholder?: string;
}

export function Select({ label, error, options, placeholder, className = '', id, ...rest }: SelectProps) {
  return (
    <div className={styles.wrapper}>
      {label && <label className={styles.label} htmlFor={id}>{label}</label>}
      <select
        id={id}
        className={[styles.select, error ? styles['select--error'] : '', className].filter(Boolean).join(' ')}
        {...rest}
      >
        {placeholder && <option value="">{placeholder}</option>}
        {options.map((opt) => (
          <option key={opt.value} value={opt.value}>{opt.label}</option>
        ))}
      </select>
      {error && <span className={styles.error}>{error}</span>}
    </div>
  );
}
