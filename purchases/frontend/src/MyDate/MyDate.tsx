import {useEffect, useState} from "react";
import Calendar from "react-calendar";
import "./MyDate.css";
import {dateFormatSpring, formatDateForUser} from "./MyDateService.ts";
import {Value} from "react-calendar/dist/shared/types";

interface FormValues {
    setValue?: any;
    watch: any;
    name: string;
    className: string;
    required: boolean;
    placeholder?: string;
    valueOnUpdate?: Date | string | undefined;
    disabled?: boolean;
}

export function MyDate({
                           name,
                           placeholder,
                           className,
                           setValue,
                           watch,
                           valueOnUpdate,
                           disabled = false
                       }: FormValues) {

    // Initialize the selectedDate from valueOnUpdate or use watch to get the current value from the form
    const [selectedDate, setSelectedDate] = useState<Date | null>(valueOnUpdate ? new Date(valueOnUpdate) : null);

    // Update the state when the form value changes
    useEffect(() => {
        if (watch) {
            const currentValue = watch(name);
            if (currentValue) {
                setSelectedDate(new Date(currentValue)); // Sync selectedDate state with the form value
            } else {
                setSelectedDate(null);
            }
        }
    }, [watch(name)]);

    const [showCalendar, setShowCalendar] = useState(false);

    const handleDateChange = (date: Value) => {
        const newDate = date as Date;
        setSelectedDate(newDate); // Update the local selectedDate state
        setValue(name, dateFormatSpring(newDate)); // Update the form value immediately using setValue
        setShowCalendar(false); // Close the calendar after selection
    };
    return (
        <>
            <input
                type="text"
                placeholder={placeholder}
                readOnly
                onClick={() => setShowCalendar(true)}
                value={selectedDate ? formatDateForUser(selectedDate) : ""}
                className={className}
                disabled={disabled}
            />

            {showCalendar && (
                <div className="calendar-overlay" onClick={() => setShowCalendar(false)}>
                    <div className="calendar-popup" onClick={(e) => e.stopPropagation()}>
                        <Calendar
                            onChange={(date) => {
                                handleDateChange(date);
                            }}
                            value={selectedDate || new Date()} // If selectedDate is null, use the current date
                            minDate={new Date()} // Prevent past dates from being selected
                        />
                    </div>
                </div>
            )}
        </>
    );
}
